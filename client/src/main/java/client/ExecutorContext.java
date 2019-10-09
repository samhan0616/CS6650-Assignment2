package client;

import constant.ArgsDefaultValue;
import constant.ArgsName;
import constant.message.ArgsMessage;
import exception.ArgsException;

public class ExecutorContext {

  public static String address;
  public static Integer numLifts = 40;
  public static Integer numRuns = 10;
  public static Integer numSkiers;
  public static Integer numThreads;
  

  public static void setValue(String name, String val) {
    switch (name) {
      case ArgsName.ADDRESS: address = val.contains("http") ? val : "http://"+ val; return;
      case ArgsName.NUM_LIFTS: numLifts = Integer.parseInt(val); return;
      case ArgsName.NUM_RUNS: numRuns = Integer.parseInt(val); return;
      case ArgsName.NUM_SKIERS: numSkiers = Integer.parseInt(val); return;
      case ArgsName.NUM_THREADS: numThreads = Integer.parseInt(val);
    }
  }

  /**
   * method to check if all fields were valued
   * @return
   */
  public static void setUp(){
    StringBuilder insufficient = new StringBuilder();
    if (address == null) {
      insufficient.append("address ");
    }
    if (numSkiers == null) {
      insufficient.append("numSkiers ");
    }
    if (numThreads == null) {
      insufficient.append("numThreads ");
    }
    if (insufficient.length() != 0) {
      throw new ArgsException(String.format(ArgsMessage.INSUFFICIENT_ARGS, insufficient.toString()));
    }
  }

}
