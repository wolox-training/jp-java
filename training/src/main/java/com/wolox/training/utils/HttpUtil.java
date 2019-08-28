package com.wolox.training.utils;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;

public class HttpUtil {

  public static String[] authorizationHeader(String header) {
    if (Strings.isNullOrEmpty(header)) {
      return null;
    }

    int numDelimiters = StringUtils.countMatches(header, ":");

    if (numDelimiters < 1) {
      return null;
    } else if (numDelimiters == 1) {
      int delim = header.indexOf(":");
      return new String[]{header.substring(0, delim), header.substring(delim + 1)};
    } else {
      return header.split(":");
    }
  }
}
