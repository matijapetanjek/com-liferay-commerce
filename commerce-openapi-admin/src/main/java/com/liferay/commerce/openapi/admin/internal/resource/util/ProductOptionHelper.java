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

package com.liferay.commerce.openapi.admin.internal.resource.util;

import com.liferay.commerce.openapi.admin.internal.util.DTOUtils;
import com.liferay.commerce.openapi.admin.internal.util.IdUtils;
import com.liferay.commerce.openapi.admin.model.ProductOptionDTO;
import com.liferay.commerce.openapi.core.context.Pagination;
import com.liferay.commerce.openapi.core.model.CollectionDTO;
import com.liferay.commerce.product.exception.NoSuchCPOptionException;
import com.liferay.commerce.product.model.CPOption;
import com.liferay.commerce.product.service.CPOptionService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Ivica Cardic
 */
@Component(immediate = true, service = ProductOptionHelper.class)
public class ProductOptionHelper {

	public void deleteProductOption(String id, Company company)
		throws PortalException {

		CPOption cpOption = null;

		try {
			cpOption = getCPOptionById(id, company);
		}
		catch (NoSuchCPOptionException nscpoe) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Product Option does not exist with ID: " + id, nscpoe);
			}

			return;
		}

		_cpOptionService.deleteCPOption(cpOption.getCPOptionId());
	}

	public CPOption getCPOptionById(String id, Company company)
		throws PortalException {

		CPOption cpOption = null;

		if (IdUtils.isLocalPK(id)) {
			cpOption = _cpOptionService.fetchCPOption(GetterUtil.getLong(id));
		}
		else {
			cpOption = _cpOptionService.fetchByExternalReferenceCode(
				company.getCompanyId(),
				IdUtils.getExternalReferenceCodeFromId(id));
		}

		if (cpOption == null) {
			throw new NoSuchCPOptionException(
				"Unable to find Product Option with ID: " + id);
		}

		return cpOption;
	}

	public ProductOptionDTO getProductOption(
			String id, String languageId, Company company)
		throws PortalException {

		return DTOUtils.modelToDTO(getCPOptionById(id, company), languageId);
	}

	public CollectionDTO<ProductOptionDTO> getProductOptions(
			long groupId, String languageId, Pagination pagination)
		throws PortalException {

		List<CPOption> cpOptions = _cpOptionService.getCPOptions(
			groupId, pagination.getStartPosition(), pagination.getEndPosition(),
			null);

		int totalItems = _cpOptionService.getCPOptionsCount(groupId);

		Stream<CPOption> stream = cpOptions.stream();

		return stream.map(
			cpOption -> DTOUtils.modelToDTO(cpOption, languageId)
		).collect(
			Collectors.collectingAndThen(
				Collectors.toList(),
				productOptionDTOs ->
					new CollectionDTO<>(productOptionDTOs, totalItems))
		);
	}

	public ProductOptionDTO updateProductOption(
			String id, long groupId, ProductOptionDTO productOptionDTO,
			String languageId, Company company)
		throws PortalException {

		CPOption cpOption = getCPOptionById(id, company);

		Map<Locale, String> nameMap = cpOption.getNameMap();

		Locale locale = LocaleUtil.fromLanguageId(languageId);

		nameMap.put(locale, productOptionDTO.getName());

		Map<Locale, String> descriptionMap = cpOption.getDescriptionMap();

		descriptionMap.put(locale, productOptionDTO.getDescription());

		cpOption = _cpOptionService.updateCPOption(
			cpOption.getCPOptionId(), nameMap, descriptionMap,
			productOptionDTO.getFieldType(), productOptionDTO.isFacetable(),
			productOptionDTO.isRequired(), productOptionDTO.isSkuContributor(),
			productOptionDTO.getKey(),
			_serviceContextHelper.getServiceContext(groupId));

		return DTOUtils.modelToDTO(cpOption, languageId);
	}

	public ProductOptionDTO upsertProductOption(
			long groupId, ProductOptionDTO productOptionDTO, String languageId)
		throws PortalException {

		Locale locale = LocaleUtil.fromLanguageId(languageId);

		Map<Locale, String> nameMap = new HashMap<Locale, String>() {
			{
				put(locale, productOptionDTO.getName());
			}
		};

		Map<Locale, String> descriptionMap = new HashMap<Locale, String>() {
			{
				put(locale, productOptionDTO.getDescription());
			}
		};

		CPOption cpOption = _cpOptionService.upsertCPOption(
			nameMap, descriptionMap, productOptionDTO.getFieldType(),
			productOptionDTO.isFacetable(), productOptionDTO.isRequired(),
			productOptionDTO.isSkuContributor(), productOptionDTO.getKey(),
			productOptionDTO.getExternalReferenceCode(),
			_serviceContextHelper.getServiceContext(groupId));

		return DTOUtils.modelToDTO(cpOption, languageId);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ProductOptionHelper.class);

	@Reference
	private CPOptionService _cpOptionService;

	@Reference
	private ServiceContextHelper _serviceContextHelper;

}