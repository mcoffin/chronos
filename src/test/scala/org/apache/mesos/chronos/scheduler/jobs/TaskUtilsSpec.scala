package org.apache.mesos.chronos.scheduler.jobs

import org.joda.time._
import org.specs2.mock._
import org.specs2.mutable._

class TaskUtilsSpec extends SpecificationWithJUnit with Mockito {

  "TaskUtils" should {
    "Get taskId" in {
      val schedule = "R/2012-01-01T00:00:01.000Z/P1M"
      val arguments = "-a 1 -b 2 -c /etc/passwd -d \\foo\\bar"
      val job1 = new ScheduleBasedJob(schedule, "sample-name", "sample-command", arguments = List(arguments))
      val job2 = new ScheduleBasedJob(schedule, "sample-name", "sample-command")
      val ts = 1420843781398L
      val due = new DateTime(ts)

      val taskIdOne = TaskUtils.getTaskId(job1, due, 0)
      val taskIdTwo = TaskUtils.getTaskId(job2, due, 0)

      taskIdOne must_== "ct:1420843781398:0:sample-name:-a 1 -b 2 -c _etc_passwd -d _foo_bar"
      taskIdTwo must_== "ct:1420843781398:0:sample-name:"
    }

    "Parse taskId" in {
      val arguments = "-a 1 -b 2"
      val arguments2 = "-a 1:2 --B test"

      val taskIdOne = "ct:1420843781398:0:test:" + arguments
      val (jobName, jobDue, attempt) = TaskUtils.parseTaskId(taskIdOne)

      jobName must_== "test"
      jobDue must_== 1420843781398L
      attempt must_== 0

      val taskIdTwo = "ct:1420843781398:0:test:" + arguments2
      val (_, _, _) = TaskUtils.parseTaskId(taskIdTwo)

      val taskIdThree = "ct:1420843781398:0:test"
      val (jobName3, _, _) = TaskUtils.parseTaskId(taskIdThree)

      jobName3 must_== "test"
    }
  }
}

