package validators;

import constant.ArgsName;

public class NumThreadsValidator extends RangeArgsValidator {
  private static int DEFAULT_VALUE = 12;
  private static String NAME = ArgsName.NUM_THREADS;
  private static int MIN = 1;
  private static int MAX = 256;

  public NumThreadsValidator() {
    super(NAME, MIN, MAX);
  }

}
