package validators;

import constant.ArgsName;
import exception.ArgsException;
import constant.message.ArgsMessage;

import java.util.regex.Pattern;

public class AddressValidator extends ArgsValidator {

  public AddressValidator() {
    this.name = ArgsName.ADDRESS;
  }

  /*
   * port range 0~65535
   * ip range max 255 per block
   */
  private static final Pattern PATTERN = Pattern
          .compile("((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})" +
                  "(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}:" +
                  "([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}" +
                  "|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])");

  @Override
  public void validate(String val) {
    if (!PATTERN.matcher(val).matches()){
      throw new ArgsException(String.format(ArgsMessage.INVALID_ARGS, this.name));
    }
  }

}
