package cl.mobdev.rm.application.service;

public class MartianTypeTranslatorService {
  public static String translateToMartian(String type) {
    return type.toLowerCase()
        .replace("a", "1")
        .replace("e", "2")
        .replace("i", "3")
        .replace("o", "4")
        .replace("u", "5")
        .replaceAll("\\s+", "");
  }
}
