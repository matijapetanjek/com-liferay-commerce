/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.data.integration.apio.internal.resource;

import static com.liferay.portal.apio.idempotent.Idempotent.idempotent;

import com.liferay.apio.architect.functional.Try;
import com.liferay.apio.architect.pagination.PageItems;
import com.liferay.apio.architect.pagination.Pagination;
import com.liferay.apio.architect.representor.Representor;
import com.liferay.apio.architect.resource.NestedCollectionResource;
import com.liferay.apio.architect.routes.ItemRoutes;
import com.liferay.apio.architect.routes.NestedCollectionRoutes;
import com.liferay.commerce.data.integration.apio.identifier.CPDefinitionIdentifier;
import com.liferay.commerce.data.integration.apio.identifier.CPInstanceIdentifier;
import com.liferay.commerce.data.integration.apio.identifier.ClassPKExternalReferenceCode;
import com.liferay.commerce.data.integration.apio.internal.form.CPInstanceUpserterForm;
import com.liferay.commerce.data.integration.apio.internal.util.CPDefinitionHelper;
import com.liferay.commerce.data.integration.apio.internal.util.CPInstanceHelper;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.service.CPInstanceService;
import com.liferay.portal.apio.permission.HasPermission;
import com.liferay.portal.apio.user.CurrentUser;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.site.apio.architect.identifier.WebSiteIdentifier;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rodrigo Guedes de Souza
 * @author Zoltán Takács
 */
@Component(immediate = true, service = NestedCollectionResource.class)
public class CPInstanceNestedCollectionResource
	implements NestedCollectionResource
		<CPInstance, ClassPKExternalReferenceCode, CPInstanceIdentifier,
		 ClassPKExternalReferenceCode, CPDefinitionIdentifier> {

	@Override
	public NestedCollectionRoutes
		<CPInstance, ClassPKExternalReferenceCode, ClassPKExternalReferenceCode>
			collectionRoutes(
				NestedCollectionRoutes.Builder
					<CPInstance, ClassPKExternalReferenceCode,
					 ClassPKExternalReferenceCode>
						builder) {

		return builder.addGetter(
			this::_getPageItems
		).addCreator(
			this::_upsertCPInstance, CurrentUser.class,
			_hasPermission.forAddingIn(CPDefinitionIdentifier.class),
			CPInstanceUpserterForm::buildForm
		).build();
	}

	@Override
	public String getName() {
		return "commerce-product-instance";
	}

	@Override
	public ItemRoutes<CPInstance, ClassPKExternalReferenceCode> itemRoutes(
		ItemRoutes.Builder<CPInstance, ClassPKExternalReferenceCode> builder) {

		return builder.addGetter(
			_cpInstanceHelper::getCPInstanceByClassPKExternalReferenceCode
		).addRemover(
			idempotent(_cpInstanceHelper::deleteCPInstance),
			_hasPermission::forDeleting
		).addUpdater(
			this::_updateCPInstance, CurrentUser.class,
			_hasPermission::forUpdating, CPInstanceUpserterForm::buildForm
		).build();
	}

	@Override
	public Representor<CPInstance> representor(
		Representor.Builder<CPInstance, ClassPKExternalReferenceCode> builder) {

		return builder.types(
			"CommerceProductInstance"
		).identifier(
			_cpInstanceHelper::cpInstanceToClassPKExternalReferenceCode
		).addBidirectionalModel(
			"commerceProductDefinition", "commerceProductInstances",
			CPDefinitionIdentifier.class,
			cpInstance -> _cpDefinitionHelper.
				cpDefinitionIdToclassPKExternalReferenceCode(
					cpInstance.getCPDefinitionId())
		).addBidirectionalModel(
			"webSite", "commerceProductInstances", WebSiteIdentifier.class,
			CPInstance::getGroupId
		).addBoolean(
			"purchasable", CPInstance::isPurchasable
		).addDate(
			"dateCreated", CPInstance::getCreateDate
		).addDate(
			"dateModified", CPInstance::getModifiedDate
		).addNumber(
			"cost", CPInstance::getCost
		).addNumber(
			"depth", CPInstance::getDepth
		).addNumber(
			"height", CPInstance::getHeight
		).addNumber(
			"price", CPInstance::getPrice
		).addNumber(
			"promoPrice", CPInstance::getPromoPrice
		).addNumber(
			"weight", CPInstance::getWeight
		).addNumber(
			"width", CPInstance::getWidth
		).addString(
			"externalReferenceCode", CPInstance::getExternalReferenceCode
		).addString(
			"gtin", CPInstance::getGtin
		).addString(
			"manufacturerPartNumber", CPInstance::getManufacturerPartNumber
		).addString(
			"productName",
			CPInstanceNestedCollectionResource::_getProductDefinitionName
		).addString(
			"sku", CPInstance::getSku
		).build();
	}

	private static String _getProductDefinitionName(CPInstance cpInstance) {
		return Try.fromFallible(
			() -> {
				CPDefinition cpDefinition = cpInstance.getCPDefinition();

				return cpDefinition.getName();
			}
		).orElse(
			null
		);
	}

	private PageItems<CPInstance> _getPageItems(
			Pagination pagination,
			ClassPKExternalReferenceCode
				cpDefinitionClassPKExternalReferenceCode)
		throws PortalException {

		CPDefinition cpDefinition =
			_cpDefinitionHelper.getCPDefinitionByClassPKExternalReferenceCode(
				cpDefinitionClassPKExternalReferenceCode);

		List<CPInstance> cpInstances =
			_cpInstanceService.getCPDefinitionInstances(
				cpDefinition.getCPDefinitionId(), WorkflowConstants.STATUS_ANY,
				pagination.getStartPosition(), pagination.getEndPosition(),
				null);

		int total = _cpInstanceService.getCPDefinitionInstancesCount(
			cpDefinition.getCPDefinitionId(), WorkflowConstants.STATUS_ANY);

		return new PageItems<>(cpInstances, total);
	}

	private CPInstance _updateCPInstance(
			ClassPKExternalReferenceCode cpInstanceCPKERC,
			CPInstanceUpserterForm cpInstanceUpserterForm, User currentUser)
		throws PortalException {

		return _cpInstanceHelper.updateCPInstance(
			cpInstanceCPKERC, cpInstanceUpserterForm.getSku(),
			cpInstanceUpserterForm.getGtin(),
			cpInstanceUpserterForm.getManufacturerPartNumber(),
			cpInstanceUpserterForm.getPurchasable(),
			cpInstanceUpserterForm.getWidth(),
			cpInstanceUpserterForm.getHeight(),
			cpInstanceUpserterForm.getDepth(),
			cpInstanceUpserterForm.getWeight(),
			cpInstanceUpserterForm.getCost(), cpInstanceUpserterForm.getPrice(),
			cpInstanceUpserterForm.getPromoPrice(),
			cpInstanceUpserterForm.getPublished(),
			cpInstanceUpserterForm.getDisplayDate(),
			cpInstanceUpserterForm.getExpirationDate(),
			cpInstanceUpserterForm.getNeverExpire(),
			cpInstanceUpserterForm.getExternalReferenceCode(), currentUser);
	}

	private CPInstance _upsertCPInstance(
			ClassPKExternalReferenceCode cpDefinitionCPKERC,
			CPInstanceUpserterForm cpInstanceUpserterForm, User currentUser)
		throws PortalException {

		return _cpInstanceHelper.upsertCPInstance(
			cpDefinitionCPKERC, cpInstanceUpserterForm.getSku(),
			cpInstanceUpserterForm.getGtin(),
			cpInstanceUpserterForm.getManufacturerPartNumber(),
			cpInstanceUpserterForm.getPurchasable(),
			cpInstanceUpserterForm.getWidth(),
			cpInstanceUpserterForm.getHeight(),
			cpInstanceUpserterForm.getDepth(),
			cpInstanceUpserterForm.getWeight(),
			cpInstanceUpserterForm.getCost(), cpInstanceUpserterForm.getPrice(),
			cpInstanceUpserterForm.getPromoPrice(),
			cpInstanceUpserterForm.getPublished(),
			cpInstanceUpserterForm.getDisplayDate(),
			cpInstanceUpserterForm.getExpirationDate(),
			cpInstanceUpserterForm.getNeverExpire(),
			cpInstanceUpserterForm.getExternalReferenceCode(), 0, currentUser);
	}

	@Reference
	private CPDefinitionHelper _cpDefinitionHelper;

	@Reference
	private CPInstanceHelper _cpInstanceHelper;

	@Reference
	private CPInstanceService _cpInstanceService;

	@Reference(
		target = "(model.class.name=com.liferay.commerce.product.model.CPInstance)"
	)
	private HasPermission<ClassPKExternalReferenceCode> _hasPermission;

}