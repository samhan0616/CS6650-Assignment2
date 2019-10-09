package client;

import client.jobs.SkierWorker;
import client.jobs.StatusListener;
import constant.PhaseEnum;
import exception.ArgsException;
import statistic.StatsComputer;
import statistic.StatsThread;
import validators.AddressValidator;
import validators.ArgsValidator;
import validators.NumLiftsValidator;
import validators.NumRunsValidator;
import validators.NumSkiersValidator;
import validators.NumThreadsValidator;
import constant.ArgsName;
import constant.message.ArgsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;


public class Executor {

  public static final double PHASE1_RUNNER_FACTOR = 0.1;
  public static final double PHASE2_RUNNER_FACTOR = 0.8;
  public static final double PHASE3_RUNNER_FACTOR = 0.1;
  public static final int PHASE1_THREAD_FACTOR = 4;
  public static final int PHASE2_THREAD_FACTOR = 1;
  public static final int PHASE3_THREAD_FACTOR = 4;

  public static final int PHASE1_TIME_FROM = 1;
  public static final int PHASE1_TIME_TO = 90;
  public static final int PHASE2_TIME_FROM = 91;
  public static final int PHASE2_TIME_TO = 360;
  public static final int PHASE3_TIME_FROM = 361;
  public static final int PHASE3_TIME_TO = 420;

  private HashMap<String, ArgsValidator> validators;

  private Logger logger = LoggerFactory.getLogger(this.getClass());


  public Executor(String[] args) {
    if (args.length == 0) {
      throw new ArgsException(ArgsMessage.EMPTY_ARGS);
    }
    init(args);
  }


  /**
   * run the main job
   */
  public void run() {
    recording();
    StatusListener.start = System.currentTimeMillis();
    // phase 1
    CountDownLatch phase1CD = new CountDownLatch((int)Math.ceil(ExecutorContext.numThreads * PHASE1_RUNNER_FACTOR / PHASE1_THREAD_FACTOR));
    doPhase(PhaseEnum.phase1, phase1CD, PHASE1_RUNNER_FACTOR, PHASE1_THREAD_FACTOR, PHASE1_TIME_FROM, PHASE1_TIME_TO);
    try {
      phase1CD.await();
    } catch (InterruptedException e) {
      logger.error("phase 1 cd error");
    }
    //phase 2
    CountDownLatch phase2CD = new CountDownLatch((int)Math.ceil(ExecutorContext.numThreads * PHASE2_RUNNER_FACTOR / PHASE2_THREAD_FACTOR));
    doPhase(PhaseEnum.phase2, phase2CD, PHASE2_RUNNER_FACTOR, PHASE2_THREAD_FACTOR, PHASE2_TIME_FROM, PHASE2_TIME_TO);
    try {
      phase2CD.await();
    } catch (InterruptedException e) {
      logger.error("phase 2 cd error");
    }
    //phase 3
    CountDownLatch phase3CD = new CountDownLatch((int)Math.ceil(ExecutorContext.numThreads * PHASE3_RUNNER_FACTOR / PHASE3_THREAD_FACTOR));
    doPhase(PhaseEnum.phase3, phase3CD, PHASE3_RUNNER_FACTOR, PHASE3_THREAD_FACTOR, PHASE3_TIME_FROM,  PHASE3_TIME_TO);
    try {
      phase3CD.await();
    } catch (InterruptedException e) {
      logger.error("phase 3 cd error");
    }
    StatusListener.completePhase();
  }

  private void recording() {
    File file = new File("record.csv");
    FileWriter csvWriter = null;
    try {
      csvWriter = new FileWriter(file);
      StatsComputer computer = new StatsComputer(ExecutorContext.numRuns * ExecutorContext.numSkiers,
              System.currentTimeMillis());
      new Thread(new StatsThread(csvWriter, computer)).start();
    } catch (IOException e) {
      e.printStackTrace();
      logger.error("CSV writer I/O exception");
    }

  }

  private void doPhase(PhaseEnum phaseEnum, CountDownLatch countDownLatch, double runnerFactor, int threadFactor, int timeFrom, int timeTo) {

    int maxThread = ExecutorContext.numThreads / threadFactor;
    int totalRequests = (int)(ExecutorContext.numRuns * runnerFactor * ExecutorContext.numSkiers);
//    Counter counter = new Counter(phaseEnum, totalRequests);
    SkierWorker skierWorker = new SkierWorker(phaseEnum, maxThread, totalRequests, timeFrom, timeTo, countDownLatch);
    skierWorker.run();
  }

  /**
   * init executor
   * @param args command arguments
   */
  private void init(String[] args){
    loadValidators();
    loadParams(args);
  }

  /**
   * load all validators
   */
  private void loadValidators() {
    validators = new HashMap<>();
    validators.put(ArgsName.ADDRESS, new AddressValidator());
    validators.put(ArgsName.NUM_LIFTS, new NumLiftsValidator());
    validators.put(ArgsName.NUM_RUNS, new NumRunsValidator());
    validators.put(ArgsName.NUM_SKIERS, new NumSkiersValidator());
    validators.put(ArgsName.NUM_THREADS, new NumThreadsValidator());
  }

  /**
   * load all params into executor context
   * @param args command line args
   */
  private void loadParams(String[] args) {
    for (int i = 0; i < args.length; i++) {
      String[] splitArgs = args[i].split("=");
      String name = splitArgs[0], val = splitArgs[1];
      if (validators.containsKey(name)) {
        validators.get(name).validate(val);
        ExecutorContext.setValue(name,val);
      } else {
        logger.info(String.format(ArgsMessage.UNREGISTERED_ARGS, name));
      }
    }
    ExecutorContext.setUp();
  }
}

