package validators;

import exception.ArgsException;
import constant.message.ArgsMessage;
import util.StringUtil;

public abstract class RangeArgsValidator extends ArgsValidator {
  private int min;
  private int max;

  RangeArgsValidator(String name, int min, int max) {
    this.name = name;
    this.min = min;
    this.max = max;
  }

  public void validate(String val) {
    if (!StringUtil.isNum(val)) {
      throw new ArgsException(String.format(ArgsMessage.INVALID_ARGS, this.name));
    }
    int v = Integer.parseInt(val);
    if (v < min || v > max) throw new ArgsException(String.format(ArgsMessage.INVALID_ARGS, this.name));
  }
}
