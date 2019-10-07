package validators;

import constant.ArgsName;

public class NumLiftsValidator extends RangeArgsValidator {
  private static String NAME = ArgsName.NUM_LIFTS;
  private static int MIN = 1;
  private static int MAX = 60;

  public NumLiftsValidator() {
    super(NAME, MIN, MAX);
  }

}
