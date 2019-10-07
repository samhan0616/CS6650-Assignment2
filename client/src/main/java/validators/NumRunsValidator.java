package validators;

import constant.ArgsName;

public class NumRunsValidator extends RangeArgsValidator {
  private static String NAME = ArgsName.NUM_RUNS;
  private static int MIN = 1;
  private static int MAX = 20;

  public NumRunsValidator() {
    super(NAME, MIN, MAX);
  }

}
