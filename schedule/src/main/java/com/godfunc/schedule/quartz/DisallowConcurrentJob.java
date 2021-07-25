package com.godfunc.schedule.quartz;

import com.godfunc.schedule.entity.JobEntity;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

@DisallowConcurrentExecution
public class DisallowConcurrentJob extends AbstractJob {
    @Override
    public void doExecute(JobExecutionContext context, JobEntity job) throws Exception {
        JobInvokeUtils.invoke(job);
    }
}
