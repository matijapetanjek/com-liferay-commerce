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
import com.liferay.commerce.batch.engine.api.job.JobExecution;
import com.liferay.commerce.batch.engine.api.job.JobExecutionListener;
import com.liferay.commerce.batch.model.CommerceBatchJob;
import com.liferay.commerce.batch.service.CommerceBatchJobLocalService;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Ivica Cardic
 */
public class JobImplTest {

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() {
		_commerceBatchJob = Mockito.mock(CommerceBatchJob.class);
		CommerceBatchJobLocalService commerceBatchJobLocalService =
			Mockito.mock(CommerceBatchJobLocalService.class);
		_itemReader = Mockito.mock(ItemReader.class);
		_itemWriter = Mockito.mock(ItemWriter.class);

		Mockito.when(
			commerceBatchJobLocalService.updateCommerceBatchJob(
				_commerceBatchJob)
		).thenReturn(
			_commerceBatchJob
		);

		_jobImpl = new JobImpl(
			commerceBatchJobLocalService, "id", "name", _itemReader,
			_itemWriter);

		_jobExecutionListener = Mockito.mock(JobExecutionListener.class);

		_jobImpl.registerJobExecutionListener(_jobExecutionListener);
	}

	@Test(expected = Exception.class)
	public void testExecuteFailure() throws Exception {
		Mockito.when(
			_itemReader.read()
		).thenThrow(
			new Exception()
		);

		JobExecution jobExecution = new JobExecution(_commerceBatchJob, null);

		try {
			_jobImpl.execute(jobExecution);
		}
		catch (Exception e) {
			Mockito.verify(
				_itemWriter, Mockito.times(0)
			).write(
				Mockito.anyList()
			);

			Mockito.verify(
				_jobExecutionListener, Mockito.times(1)
			).afterJob(
				jobExecution
			);
			Mockito.verify(
				_jobExecutionListener, Mockito.times(1)
			).beforeJob(
				jobExecution
			);

			Mockito.verify(
				_commerceBatchJob
			).setStatus(
				BatchStatus.STARTED.toString()
			);

			Mockito.verify(
				_commerceBatchJob
			).setStatus(
				BatchStatus.FAILED.toString()
			);

			throw e;
		}
	}

	@Test
	public void testExecuteSuccess() throws Exception {
		JobExecution jobExecution = new JobExecution(_commerceBatchJob, null);

		_jobImpl.execute(jobExecution);

		Mockito.when(
			_itemReader.read()
		).thenReturn(
			new Object()
		);

		Mockito.verify(
			_itemWriter, Mockito.times(1)
		).write(
			Mockito.anyList()
		);

		Mockito.verify(
			_jobExecutionListener, Mockito.times(1)
		).afterJob(
			jobExecution
		);
		Mockito.verify(
			_jobExecutionListener, Mockito.times(1)
		).beforeJob(
			jobExecution
		);

		Mockito.verify(
			_commerceBatchJob
		).setStatus(
			BatchStatus.COMPLETED.toString()
		);
		Mockito.verify(
			_commerceBatchJob
		).setStatus(
			BatchStatus.STARTED.toString()
		);
	}

	private CommerceBatchJob _commerceBatchJob;
	private ItemReader<Object> _itemReader;
	private ItemWriter<Object> _itemWriter;
	private JobExecutionListener _jobExecutionListener;
	private JobImpl _jobImpl;

}