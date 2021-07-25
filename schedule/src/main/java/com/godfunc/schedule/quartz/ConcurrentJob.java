package com.godfunc.schedule.quartz;

import com.godfunc.schedule.entity.JobEntity;
import org.quartz.JobExecutionContext;

public class ConcurrentJob extends AbstractJob {
    @Override
    public void doExecute(JobExecutionContext context, JobEntity job) throws Exception {
        JobInvokeUtils.invoke(job);
    }
}
