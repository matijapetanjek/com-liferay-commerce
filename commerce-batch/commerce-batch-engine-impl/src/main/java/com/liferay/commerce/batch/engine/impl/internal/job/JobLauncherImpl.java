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

package com.liferay.commerce.batch.engine.impl.internal.job;

import com.liferay.commerce.batch.engine.api.job.Job;
import com.liferay.commerce.batch.engine.api.job.JobExecution;
import com.liferay.commerce.batch.engine.api.job.JobLauncher;
import com.liferay.commerce.batch.engine.api.job.JobParameters;
import com.liferay.commerce.batch.engine.impl.internal.concurrent.BlockingExecutor;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.liferay.commerce.batch.model.CommerceBatchJob;
import com.liferay.commerce.batch.service.CommerceBatchJobLocalService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Ivica Cardic
 */
@Component(immediate = true, service = JobLauncher.class)
public class JobLauncherImpl implements JobLauncher {

	public JobLauncherImpl() {
		_blockingExecutor = new BlockingExecutor(2, 10);
	}

	public JobLauncherImpl(
		BlockingExecutor blockingExecutor,
		CommerceBatchJobLocalService commerceBatchJobLocalService) {

		_blockingExecutor = Objects.requireNonNull(blockingExecutor);
		_commerceBatchJobLocalService = commerceBatchJobLocalService;
	}

	@Override
	public JobExecution run(Job job, JobParameters jobParameters) {
		Objects.requireNonNull(job);

		CommerceBatchJob commerceBatchJob =
			_commerceBatchJobLocalService.addCommerceBatchJob(
				job.getKey(), job.getName());

		JobExecution jobExecution = new JobExecution(
			commerceBatchJob, jobParameters);

		_blockingExecutor.execute(new JobRunnable(job, jobExecution));

		return jobExecution;
	}

	@Deactivate
	protected void deactivate() {
		_blockingExecutor.destroy();
	}

	@Reference
	private CommerceBatchJobLocalService _commerceBatchJobLocalService;

	private final BlockingExecutor _blockingExecutor;

}