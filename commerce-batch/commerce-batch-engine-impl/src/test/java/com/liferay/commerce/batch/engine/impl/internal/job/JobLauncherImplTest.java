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

import com.liferay.commerce.batch.engine.api.item.ItemReader;
import com.liferay.commerce.batch.engine.api.item.ItemWriter;
import com.liferay.commerce.batch.engine.api.job.BatchStatus;
import com.liferay.commerce.batch.engine.api.job.Job;
import com.liferay.commerce.batch.engine.api.job.JobExecution;
import com.liferay.commerce.batch.engine.api.job.JobParameters;
import com.liferay.commerce.batch.engine.impl.internal.concurrent.BlockingExecutor;
import com.liferay.commerce.batch.model.CommerceBatchJob;
import com.liferay.commerce.batch.service.CommerceBatchJobLocalService;

import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Ivica Cardic
 */
public class JobLauncherImplTest {

	@Test
	public void testRun() throws Exception {
		CommerceBatchJob commerceBatchJob = Mockito.mock(
			CommerceBatchJob.class);

		CommerceBatchJobLocalService commerceBatchJobLocalService =
			Mockito.mock(CommerceBatchJobLocalService.class);

		Mockito.when(
			commerceBatchJobLocalService.addCommerceBatchJob(
				Mockito.anyString(), Mockito.anyString())
		).thenReturn(
			commerceBatchJob
		);

		Mockito.when(
			commerceBatchJobLocalService.updateCommerceBatchJob(
				commerceBatchJob)
		).thenReturn(
			commerceBatchJob
		);

		JobLauncherImpl jobLauncherImpl = new JobLauncherImpl(
			new BlockingExecutor(2, 10), commerceBatchJobLocalService);

		Job job = new JobImpl(
			commerceBatchJobLocalService, "id", "name",
			Mockito.mock(ItemReader.class), Mockito.mock(ItemWriter.class));

		JobExecution jobExecution = jobLauncherImpl.run(
			job, new JobParameters());

		//		Thread.sleep(1000);

		commerceBatchJob = jobExecution.getCommerceBatchJob();

		Mockito.verify(
			commerceBatchJob
		).setStatus(
			BatchStatus.STARTED.toString()
		);

		Mockito.verify(
			commerceBatchJob
		).setStatus(
			BatchStatus.COMPLETED.toString()
		);
	}

}