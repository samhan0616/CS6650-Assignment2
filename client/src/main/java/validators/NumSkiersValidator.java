package validators;

import constant.ArgsName;

public class NumSkiersValidator extends RangeArgsValidator {
  private static int DEFAULT_VALUE = 500;
  private static String NAME = ArgsName.NUM_SKIERS;
  private static int MIN = 1;
  private static int MAX = 50000;

  public NumSkiersValidator() {
    super(NAME, MIN, MAX);
  }

}
