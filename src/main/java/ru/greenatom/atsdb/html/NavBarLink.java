package ru.greenatom.atsdb.html;

public record NavBarLink(String url, String text) {

    public static NavBarLink mainMenu() {
        return new NavBarLink("/ats/mainMenu","Основное меню");
    };

    public static NavBarLink startMenu() {
        return new NavBarLink("#","Стартовая страница");
    }
}
