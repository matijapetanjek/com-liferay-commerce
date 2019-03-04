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

package com.liferay.commerce.batch.service.base;

import com.liferay.commerce.batch.model.CommerceBatchJobInstance;
import com.liferay.commerce.batch.service.CommerceBatchJobInstanceService;
import com.liferay.commerce.batch.service.persistence.CommerceBatchJobExecutionPersistence;
import com.liferay.commerce.batch.service.persistence.CommerceBatchJobInstancePersistence;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.service.BaseServiceImpl;
import com.liferay.portal.kernel.service.persistence.ClassNamePersistence;
import com.liferay.portal.kernel.service.persistence.UserPersistence;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the commerce batch job instance remote service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.commerce.batch.service.impl.CommerceBatchJobInstanceServiceImpl}.
 * </p>
 *
 * @author Matija Petanjek
 * @see com.liferay.commerce.batch.service.impl.CommerceBatchJobInstanceServiceImpl
 * @see com.liferay.commerce.batch.service.CommerceBatchJobInstanceServiceUtil
 * @generated
 */
public abstract class CommerceBatchJobInstanceServiceBaseImpl
	extends BaseServiceImpl implements CommerceBatchJobInstanceService,
		IdentifiableOSGiService {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link com.liferay.commerce.batch.service.CommerceBatchJobInstanceServiceUtil} to access the commerce batch job instance remote service.
	 */

	/**
	 * Returns the commerce batch job execution local service.
	 *
	 * @return the commerce batch job execution local service
	 */
	public com.liferay.commerce.batch.service.CommerceBatchJobExecutionLocalService getCommerceBatchJobExecutionLocalService() {
		return commerceBatchJobExecutionLocalService;
	}

	/**
	 * Sets the commerce batch job execution local service.
	 *
	 * @param commerceBatchJobExecutionLocalService the commerce batch job execution local service
	 */
	public void setCommerceBatchJobExecutionLocalService(
		com.liferay.commerce.batch.service.CommerceBatchJobExecutionLocalService commerceBatchJobExecutionLocalService) {
		this.commerceBatchJobExecutionLocalService = commerceBatchJobExecutionLocalService;
	}

	/**
	 * Returns the commerce batch job execution remote service.
	 *
	 * @return the commerce batch job execution remote service
	 */
	public com.liferay.commerce.batch.service.CommerceBatchJobExecutionService getCommerceBatchJobExecutionService() {
		return commerceBatchJobExecutionService;
	}

	/**
	 * Sets the commerce batch job execution remote service.
	 *
	 * @param commerceBatchJobExecutionService the commerce batch job execution remote service
	 */
	public void setCommerceBatchJobExecutionService(
		com.liferay.commerce.batch.service.CommerceBatchJobExecutionService commerceBatchJobExecutionService) {
		this.commerceBatchJobExecutionService = commerceBatchJobExecutionService;
	}

	/**
	 * Returns the commerce batch job execution persistence.
	 *
	 * @return the commerce batch job execution persistence
	 */
	public CommerceBatchJobExecutionPersistence getCommerceBatchJobExecutionPersistence() {
		return commerceBatchJobExecutionPersistence;
	}

	/**
	 * Sets the commerce batch job execution persistence.
	 *
	 * @param commerceBatchJobExecutionPersistence the commerce batch job execution persistence
	 */
	public void setCommerceBatchJobExecutionPersistence(
		CommerceBatchJobExecutionPersistence commerceBatchJobExecutionPersistence) {
		this.commerceBatchJobExecutionPersistence = commerceBatchJobExecutionPersistence;
	}

	/**
	 * Returns the commerce batch job instance local service.
	 *
	 * @return the commerce batch job instance local service
	 */
	public com.liferay.commerce.batch.service.CommerceBatchJobInstanceLocalService getCommerceBatchJobInstanceLocalService() {
		return commerceBatchJobInstanceLocalService;
	}

	/**
	 * Sets the commerce batch job instance local service.
	 *
	 * @param commerceBatchJobInstanceLocalService the commerce batch job instance local service
	 */
	public void setCommerceBatchJobInstanceLocalService(
		com.liferay.commerce.batch.service.CommerceBatchJobInstanceLocalService commerceBatchJobInstanceLocalService) {
		this.commerceBatchJobInstanceLocalService = commerceBatchJobInstanceLocalService;
	}

	/**
	 * Returns the commerce batch job instance remote service.
	 *
	 * @return the commerce batch job instance remote service
	 */
	public CommerceBatchJobInstanceService getCommerceBatchJobInstanceService() {
		return commerceBatchJobInstanceService;
	}

	/**
	 * Sets the commerce batch job instance remote service.
	 *
	 * @param commerceBatchJobInstanceService the commerce batch job instance remote service
	 */
	public void setCommerceBatchJobInstanceService(
		CommerceBatchJobInstanceService commerceBatchJobInstanceService) {
		this.commerceBatchJobInstanceService = commerceBatchJobInstanceService;
	}

	/**
	 * Returns the commerce batch job instance persistence.
	 *
	 * @return the commerce batch job instance persistence
	 */
	public CommerceBatchJobInstancePersistence getCommerceBatchJobInstancePersistence() {
		return commerceBatchJobInstancePersistence;
	}

	/**
	 * Sets the commerce batch job instance persistence.
	 *
	 * @param commerceBatchJobInstancePersistence the commerce batch job instance persistence
	 */
	public void setCommerceBatchJobInstancePersistence(
		CommerceBatchJobInstancePersistence commerceBatchJobInstancePersistence) {
		this.commerceBatchJobInstancePersistence = commerceBatchJobInstancePersistence;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public com.liferay.counter.kernel.service.CounterLocalService getCounterLocalService() {
		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(
		com.liferay.counter.kernel.service.CounterLocalService counterLocalService) {
		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the class name local service.
	 *
	 * @return the class name local service
	 */
	public com.liferay.portal.kernel.service.ClassNameLocalService getClassNameLocalService() {
		return classNameLocalService;
	}

	/**
	 * Sets the class name local service.
	 *
	 * @param classNameLocalService the class name local service
	 */
	public void setClassNameLocalService(
		com.liferay.portal.kernel.service.ClassNameLocalService classNameLocalService) {
		this.classNameLocalService = classNameLocalService;
	}

	/**
	 * Returns the class name remote service.
	 *
	 * @return the class name remote service
	 */
	public com.liferay.portal.kernel.service.ClassNameService getClassNameService() {
		return classNameService;
	}

	/**
	 * Sets the class name remote service.
	 *
	 * @param classNameService the class name remote service
	 */
	public void setClassNameService(
		com.liferay.portal.kernel.service.ClassNameService classNameService) {
		this.classNameService = classNameService;
	}

	/**
	 * Returns the class name persistence.
	 *
	 * @return the class name persistence
	 */
	public ClassNamePersistence getClassNamePersistence() {
		return classNamePersistence;
	}

	/**
	 * Sets the class name persistence.
	 *
	 * @param classNamePersistence the class name persistence
	 */
	public void setClassNamePersistence(
		ClassNamePersistence classNamePersistence) {
		this.classNamePersistence = classNamePersistence;
	}

	/**
	 * Returns the resource local service.
	 *
	 * @return the resource local service
	 */
	public com.liferay.portal.kernel.service.ResourceLocalService getResourceLocalService() {
		return resourceLocalService;
	}

	/**
	 * Sets the resource local service.
	 *
	 * @param resourceLocalService the resource local service
	 */
	public void setResourceLocalService(
		com.liferay.portal.kernel.service.ResourceLocalService resourceLocalService) {
		this.resourceLocalService = resourceLocalService;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public com.liferay.portal.kernel.service.UserLocalService getUserLocalService() {
		return userLocalService;
	}

	/**
	 * Sets the user local service.
	 *
	 * @param userLocalService the user local service
	 */
	public void setUserLocalService(
		com.liferay.portal.kernel.service.UserLocalService userLocalService) {
		this.userLocalService = userLocalService;
	}

	/**
	 * Returns the user remote service.
	 *
	 * @return the user remote service
	 */
	public com.liferay.portal.kernel.service.UserService getUserService() {
		return userService;
	}

	/**
	 * Sets the user remote service.
	 *
	 * @param userService the user remote service
	 */
	public void setUserService(
		com.liferay.portal.kernel.service.UserService userService) {
		this.userService = userService;
	}

	/**
	 * Returns the user persistence.
	 *
	 * @return the user persistence
	 */
	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	/**
	 * Sets the user persistence.
	 *
	 * @param userPersistence the user persistence
	 */
	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	public void afterPropertiesSet() {
	}

	public void destroy() {
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return CommerceBatchJobInstanceService.class.getName();
	}

	protected Class<?> getModelClass() {
		return CommerceBatchJobInstance.class;
	}

	protected String getModelClassName() {
		return CommerceBatchJobInstance.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = commerceBatchJobInstancePersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(dataSource,
					sql);

			sqlUpdate.update();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@BeanReference(type = com.liferay.commerce.batch.service.CommerceBatchJobExecutionLocalService.class)
	protected com.liferay.commerce.batch.service.CommerceBatchJobExecutionLocalService commerceBatchJobExecutionLocalService;
	@BeanReference(type = com.liferay.commerce.batch.service.CommerceBatchJobExecutionService.class)
	protected com.liferay.commerce.batch.service.CommerceBatchJobExecutionService commerceBatchJobExecutionService;
	@BeanReference(type = CommerceBatchJobExecutionPersistence.class)
	protected CommerceBatchJobExecutionPersistence commerceBatchJobExecutionPersistence;
	@BeanReference(type = com.liferay.commerce.batch.service.CommerceBatchJobInstanceLocalService.class)
	protected com.liferay.commerce.batch.service.CommerceBatchJobInstanceLocalService commerceBatchJobInstanceLocalService;
	@BeanReference(type = CommerceBatchJobInstanceService.class)
	protected CommerceBatchJobInstanceService commerceBatchJobInstanceService;
	@BeanReference(type = CommerceBatchJobInstancePersistence.class)
	protected CommerceBatchJobInstancePersistence commerceBatchJobInstancePersistence;
	@ServiceReference(type = com.liferay.counter.kernel.service.CounterLocalService.class)
	protected com.liferay.counter.kernel.service.CounterLocalService counterLocalService;
	@ServiceReference(type = com.liferay.portal.kernel.service.ClassNameLocalService.class)
	protected com.liferay.portal.kernel.service.ClassNameLocalService classNameLocalService;
	@ServiceReference(type = com.liferay.portal.kernel.service.ClassNameService.class)
	protected com.liferay.portal.kernel.service.ClassNameService classNameService;
	@ServiceReference(type = ClassNamePersistence.class)
	protected ClassNamePersistence classNamePersistence;
	@ServiceReference(type = com.liferay.portal.kernel.service.ResourceLocalService.class)
	protected com.liferay.portal.kernel.service.ResourceLocalService resourceLocalService;
	@ServiceReference(type = com.liferay.portal.kernel.service.UserLocalService.class)
	protected com.liferay.portal.kernel.service.UserLocalService userLocalService;
	@ServiceReference(type = com.liferay.portal.kernel.service.UserService.class)
	protected com.liferay.portal.kernel.service.UserService userService;
	@ServiceReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
}