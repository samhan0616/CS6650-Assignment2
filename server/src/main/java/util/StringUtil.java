package util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
  public StringUtil() {
  }

  public static String FilterNull(Object o) {
    return o != null && !"null".equals(o.toString()) ? o.toString().trim() : "";
  }

  public static boolean isEmpty(Object o) {
    return o == null ? true : "".equals(FilterNull(o.toString()));
  }

  public static boolean isNotEmpty(Object o) {
    return o == null ? false : !"".equals(FilterNull(o.toString()));
  }

  public static boolean isNum(Object o) {
    String s = o.toString();
    for (char c : s.toCharArray()) {
      if (!Character.isDigit(c)) return false;
    }
    return true;
  }

  public static boolean isLong(Object o) {
    try {
      new Long(o.toString());
      return true;
    } catch (Exception var2) {
      return false;
    }
  }

  public static Long toLong(Object o) {
    return isLong(o) ? new Long(o.toString()) : Long.valueOf(0L);
  }

  public static int toInt(Object o) {
    return isNum(o) ? (new Integer(o.toString())).intValue() : 0;
  }

  public static String holdmaxlength(Object o) {
    byte maxlength = 50;
    return o == null ? "" : subStringByByte(o, maxlength);
  }

  public static String holdmaxlength(Object o, int maxlength) {
    maxlength = maxlength <= 0 ? 50 : maxlength;
    return o == null ? "" : subStringByByte(o, maxlength);
  }

  private static String subStringByByte(Object o, int len) {
    if (o == null) {
      return "";
    } else {
      String str = o.toString();
      String result = null;
      if (str != null) {
        byte[] a = str.getBytes();
        if (a.length <= len) {
          result = str;
        } else if (len > 0) {
          result = new String(a, 0, len);
          int length = result.length();
          if (str.charAt(length - 1) != result.charAt(length - 1)) {
            if (length < 2) {
              result = null;
            } else {
              result = result.substring(0, length - 1);
            }
          }
        }
      }

      return result;
    }
  }

  public static String comma_add(String commaexpress, String newelement) {
    return comma_rect(FilterNull(commaexpress) + "," + FilterNull(newelement));
  }

  public static String comma_del(String commaexpress, String delelement) {
    if (commaexpress != null && delelement != null && !commaexpress.trim().equals(delelement.trim())) {
      String[] deletelist = delelement.split(",");
      String result = commaexpress;
      String[] var4 = deletelist;
      int var5 = deletelist.length;

      for (int var6 = 0; var6 < var5; ++var6) {
        String delstr = var4[var6];
        result = comma_delone(result, delstr);
      }

      return result;
    } else {
      return "";
    }
  }

  public static String comma_delone(String commaexpress, String delelement) {
    if (commaexpress != null && delelement != null && !commaexpress.trim().equals(delelement.trim())) {
      String[] strlist = commaexpress.split(",");
      StringBuffer result = new StringBuffer();
      String[] var4 = strlist;
      int var5 = strlist.length;

      for (int var6 = 0; var6 < var5; ++var6) {
        String str = var4[var6];
        if (!str.trim().equals(delelement.trim()) && !"".equals(str.trim())) {
          result.append(str.trim() + ",");
        }
      }

      return result.toString().substring(0, result.length() - 1 > 0 ? result.length() - 1 : 0);
    } else {
      return "";
    }
  }

  public static boolean comma_contains(String commaexpress, String element) {
    boolean flag = false;
    commaexpress = FilterNull(commaexpress);
    element = FilterNull(element);
    if (!"".equals(commaexpress) && !"".equals(element)) {
      String[] strlist = commaexpress.split(",");
      String[] var4 = strlist;
      int var5 = strlist.length;

      for (int var6 = 0; var6 < var5; ++var6) {
        String str = var4[var6];
        if (str.trim().equals(element.trim())) {
          flag = true;
          break;
        }
      }
    }

    return flag;
  }

  public static String comma_intersect(String commaexpressA, String commaexpressB) {
    commaexpressA = FilterNull(commaexpressA);
    commaexpressB = FilterNull(commaexpressB);
    StringBuffer result = new StringBuffer();
    String[] strlistA = commaexpressA.split(",");
    String[] strlistB = commaexpressB.split(",");
    String[] var5 = strlistA;
    int var6 = strlistA.length;

    for (int var7 = 0; var7 < var6; ++var7) {
      String boA = var5[var7];
      String[] var9 = strlistB;
      int var10 = strlistB.length;

      for (int var11 = 0; var11 < var10; ++var11) {
        String boB = var9[var11];
        if (boA.trim().equals(boB.trim())) {
          result.append(boA.trim() + ",");
        }
      }
    }

    return comma_rect(result.toString());
  }

  public static String comma_rect(String commaexpress) {
    commaexpress = FilterNull(commaexpress);
    String[] strlist = commaexpress.split(",");
    StringBuffer result = new StringBuffer();
    String[] var3 = strlist;
    int var4 = strlist.length;

    for (int var5 = 0; var5 < var4; ++var5) {
      String str = var3[var5];
      if (!"".equals(str.trim()) && !("," + result.toString() + ",").contains("," + str + ",") && !"null".equals(str)) {
        result.append(str.trim() + ",");
      }
    }

    return result.toString().substring(0, result.length() - 1 > 0 ? result.length() - 1 : 0);
  }

  public static String comma_reverse(String commaexpress) {
    commaexpress = FilterNull(commaexpress);
    String[] ids = commaexpress.split(",");
    StringBuffer str = new StringBuffer();

    for (int i = ids.length - 1; i >= 0; --i) {
      str.append(ids[i] + ",");
    }

    return comma_rect(str.toString());
  }

  public static String comma_first(String commaexpress) {
    commaexpress = FilterNull(commaexpress);
    String[] ids = commaexpress.split(",");
    return ids != null && ids.length > 0 ? ids[0] : null;
  }

  public static String comma_last(String commaexpress) {
    commaexpress = FilterNull(commaexpress);
    String[] ids = commaexpress.split(",");
    return ids != null && ids.length > 0 ? ids[ids.length - 1] : null;
  }

  public static String replace(String strData, String regex, String replacement) {
    return strData == null ? "" : strData.replaceAll(regex, replacement);
  }

  public static String String2HTML(String strData) {
    if (strData != null && !"".equals(strData)) {
      strData = replace(strData, "&", "&amp;");
      strData = replace(strData, "<", "&lt;");
      strData = replace(strData, ">", "&gt;");
      strData = replace(strData, "\"", "&quot;");
      return strData;
    } else {
      return "";
    }
  }

  public static String getexceptionInfo(Exception e) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    try {
      e.printStackTrace(new PrintStream(baos));
    } finally {
      try {
        baos.close();
      } catch (IOException var8) {
        var8.printStackTrace();
      }

    }

    return baos.toString();
  }

  public static String regex(String str) {
    Pattern pattern = Pattern.compile("[0-9-:/ ]");
    char[] array = str.toCharArray();

    for (int i = 0; i < array.length; ++i) {
      Matcher matcher = pattern.matcher(String.valueOf(array[i]));
      if (!matcher.matches()) {
        str = str.replace(String.valueOf(array[i]), "");
      }
    }

    return str;
  }

  public static String comma_insert(String commaexpress, String newelement, int index) {
    int length = commaexpress.length();
    if (index > length) {
      index = length;
    } else if (index < 0) {
      index = 0;
    }

    String result = commaexpress.substring(0, index) + newelement + commaexpress.substring(index, commaexpress.length());
    return result;
  }

  public static String changeDirection(String strDir) {
    String s = "/";
    String a = "\\";
    if (strDir != null && !" ".equals(strDir) && strDir.contains(s)) {
      strDir = strDir.replace(s, a);
    }

    return strDir;
  }

  public static String trim(String s) {
    int i = s.length();
    int j = 0;
    byte k = 0;

    char[] arrayOfChar;
    for (arrayOfChar = s.toCharArray(); j < i && arrayOfChar[k + j] <= 32; ++j) {
      ;
    }

    while (j < i && arrayOfChar[k + i - 1] <= 32) {
      --i;
    }

    return j <= 0 && i >= s.length() ? s : s.substring(j, i);
  }

  public static String getBrackets(String str) {
    int a = str.indexOf("{");
    int c = str.indexOf("}");
    return a >= 0 && c >= 0 & c > a ? str.substring(a + 1, c) : str;
  }

  public static String commaToVerti(String str) {
    return str != null && !"".equals(str) && str.contains(",") ? str.replaceAll(",", "|") : str;
  }

  public static String extractBlank(String name) {
    return name != null && !"".equals(name) ? name.replaceAll(" +", "") : name;
  }

  public static String ConvertStr(String str) {
    return str != null && !"null".equals(str) ? str.trim() : "";
  }

  public static String removeStr(String src, String str) {
    if (src == null || str == null) return src;
    int idx = src.indexOf(str);
    if (idx == -1) return src;
    int pst = 0;
    char[] cs = src.toCharArray();
    char[] rs = new char[src.length() - str.length()];
    for (int i = 0; i < cs.length; i++) {
      if (i >= idx && i < idx + str.length()) continue;
      rs[pst] = cs[i];
      pst++;
    }
    return new String(rs);
  }

  public static String replaceStr(String src, String target, String replacement) {
    if (src == null || target == null || replacement == null) return src;
    int idx = src.indexOf(target);
    if (idx == -1) return src;
    int pst = 0;
    char[] cs = src.toCharArray();
    char[] rs = new char[src.length() - target.length() + replacement.length()];
    for (int i = 0; i < cs.length; i++) {
      if (i == idx) {
        for (char c : replacement.toCharArray()) {
          rs[pst] = c;
          pst++;
        }
        continue;
      }
      if (i > idx && i < idx + target.length()) continue;
      rs[pst] = cs[i];
      pst++;
    }
    return new String(rs);
  }

  /**
   * @param src
   * @param target
   * @param replacement
   * @return
   */
  public static String replaceAllStr(String src, String target, String replacement) {
    if (src == null || target == null || replacement == null) return src;
    int idx = src.indexOf(target);
    if (idx == -1) return src;
    int pst = 0;
    char[] cs = src.toCharArray();
    char[] rs = new char[src.length() - target.length() + replacement.length()];
    for (int i = 0; i < cs.length; i++) {
      if (i == idx) {
        for (char c : replacement.toCharArray()) {
          rs[pst] = c;
          pst++;
        }
        continue;
      }
      if (i > idx && i < idx + target.length()) continue;
      rs[pst] = cs[i];
      pst++;
    }
    return replaceAllStr(new String(rs), target, replacement);
  }

}