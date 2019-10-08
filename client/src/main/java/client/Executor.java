package client;

import client.jobs.Counter;
import client.jobs.PhraseLock;
import client.jobs.SkierWorker;
import client.jobs.StatusListener;
import constant.PhraseEnum;
import exception.ArgsException;
import io.swagger.client.ApiClient;
import io.swagger.client.api.ResortsApi;
import statistic.StatsComputer;
import statistic.StatsThread;
import util.HttpUtil;
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


public class Executor {

  public static final double PHRASE1_RUNNER_FACTOR = 0.1;
  public static final double PHRASE2_RUNNER_FACTOR = 0.8;
  public static final double PHRASE3_RUNNER_FACTOR = 0.1;
  public static final int PHRASE1_THREAD_FACTOR = 4;
  public static final int PHRASE2_THREAD_FACTOR = 1;
  public static final int PHRASE3_THREAD_FACTOR = 4;

  public static final int PHRASE1_TIME_FROM = 1;
  public static final int PHRASE1_TIME_TO = 90;
  public static final int PHRASE2_TIME_FROM = 91;
  public static final int PHRASE2_TIME_TO = 360;
  public static final int PHRASE3_TIME_FROM = 361;
  public static final int PHRASE3_TIME_TO = 420;

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
    // phrase 1
    doPhrase(PhraseEnum.phrase1, null,
            PhraseLock.phrase2Lock, PHRASE1_RUNNER_FACTOR, PHRASE1_THREAD_FACTOR, PHRASE1_TIME_FROM, PHRASE1_TIME_TO);
    //phrase 2
    doPhrase(PhraseEnum.phrase2, PhraseLock.phrase2Lock,
            PhraseLock.phrase3Lock, PHRASE2_RUNNER_FACTOR, PHRASE2_THREAD_FACTOR, PHRASE2_TIME_FROM, PHRASE2_TIME_TO);
    //phrase 3
    doPhrase(PhraseEnum.phrase3, PhraseLock.phrase3Lock,
            null, PHRASE3_RUNNER_FACTOR, PHRASE3_THREAD_FACTOR, PHRASE3_TIME_FROM,  PHRASE3_TIME_TO);
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

  private void doPhrase(PhraseEnum phraseEnum, final Object curr, Object next,
                       double runnerFactor, int threadFactor, int timeFrom, int timeTo) {
    if (curr != null) {
      synchronized (curr){
        try {
          curr.wait();
        } catch (InterruptedException e) {
          logger.error("Failed to grab the lock for " + phraseEnum);
        }
      }
    }
    int maxThread = ExecutorContext.numThreads / threadFactor;
    int totalRequests = (int)(ExecutorContext.numRuns * runnerFactor * ExecutorContext.numSkiers);
    Counter counter = new Counter(phraseEnum, next, totalRequests);
    SkierWorker skierWorker = new SkierWorker(maxThread,timeFrom, timeTo, counter);
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

