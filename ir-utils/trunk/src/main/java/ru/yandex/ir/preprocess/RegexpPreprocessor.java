package ru.yandex.ir.preprocess;

/**
 * 
 * @author Egor Azanov, <a href="mailto:krondix@yandex-team.ru">
 */
public class RegexpPreprocessor implements Preprocessor {
    public String preprocess(String string) {
        return (" " + string + " ").replaceAll("nbsp", " ").replaceAll(" wi-fi ", " wifi ").replaceAll(" printer ", " принтер ").replaceAll(" scaner ", " сканер ").replaceAll(" copier ", " копир ").replaceAll("(\\A|\\s)к-ж(\\s|\\z)", " картридж ").replaceAll(" toner ", " тонер ").replaceAll(" cartridge ", " картридж ").replaceAll(" drum unit ", " блок барабанов ").replaceAll(" drumunit ", " блок барабанов ").replaceAll(" drum ", " барабан ").replaceAll(" барабанов ", " барабан ").replaceAll(" lcd ", " жк ").replaceAll(" secure digital", " sd ").replaceAll(" securedigital ", " sd ").replaceAll(" compact flash ", " cf ").replaceAll(" compactflash ", " cf ");
    }
}
