package ru.yandex.ir.preprocess;

/**
 * 
 * @author Egor Azanov, <a href="mailto:krondix@yandex-team.ru">
 */
public class RegexpPreprocessor implements Preprocessor {
    public String preprocess(String string) {
        return (" " + string + " ").replaceAll("nbsp", " ").replaceAll(" wi-fi ", " wifi ").replaceAll(" printer ", " ������� ").replaceAll(" scaner ", " ������ ").replaceAll(" copier ", " ����� ").replaceAll("(\\A|\\s)�-�(\\s|\\z)", " �������� ").replaceAll(" toner ", " ����� ").replaceAll(" cartridge ", " �������� ").replaceAll(" drum unit ", " ���� ��������� ").replaceAll(" drumunit ", " ���� ��������� ").replaceAll(" drum ", " ������� ").replaceAll(" ��������� ", " ������� ").replaceAll(" lcd ", " �� ").replaceAll(" secure digital", " sd ").replaceAll(" securedigital ", " sd ").replaceAll(" compact flash ", " cf ").replaceAll(" compactflash ", " cf ");
    }
}
