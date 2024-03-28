/**************************************************************************
 *                                                                        *
 *  File:        Menu.cs                                                  *
 *  Copyright:   (c) 2024, Moloman Laurentiu-Ionut                        *
 *  E-mail:      laurentiu-ionut.moloman@student.tuiasi.ro                *
 *  Description: TransportInfo application with MVC architecture.         *
 *               Menu class.(Software Engineering lab 6)                  *
 *                                                                        *
 *  This program is free software; you can redistribute it and/or modify  *
 *  it under the terms of the GNU General Public License as published by  *
 *  the Free Software Foundation. This program is distributed in the      *
 *  hope that it will be useful, but WITHOUT ANY WARRANTY; without even   *
 *  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR   *
 *  PURPOSE. See the GNU General Public License for more details.         *
 *                                                                        *
 **************************************************************************/


using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace View
{
    public class Menus
    {
        // structuri de date

        public enum UserChoice { AdminMenu, UserMenu, PreviousMenu, Route, AddCity, RemoveCity, Exit, List, Undefined };
        public enum MenuState { Main, Administrator, User };

        public struct MenuOption
        {
            // structura pentru construirea dinamica a unui meniu
            // reprezinta o optiune intr-un meniu

            public readonly string Number;
            public readonly string Text;
            public readonly UserChoice Choice;

            public MenuOption(string number, string text, UserChoice choice)
            {
                Number = number;
                Text = text;
                Choice = choice;
            }
        }

        // metodele de mai jos trebuie plasate in clasele potrivite
        public static void MainMenu(out List<MenuOption> options, out string action)
        {
            action = "Selectati rolul";
            options = new List<MenuOption>(3);
            options.Add(new MenuOption("1", "Utilizator", UserChoice.UserMenu));
            options.Add(new MenuOption("2", "Administrator", UserChoice.AdminMenu));
            options.Add(new MenuOption("3", "Iesire", UserChoice.Exit));
        }

        public static void AdminMenu(out List<MenuOption> options, out string action)
        {
            action = "Selectati actiunea dorita";
            options = new List<MenuOption>(5);
            options.Add(new MenuOption("1", "Afisarea tuturor oraselor", UserChoice.List));
            options.Add(new MenuOption("2", "Introducerea unui nou oras", UserChoice.AddCity));
            options.Add(new MenuOption("3", "Stergerea unui oras", UserChoice.RemoveCity));
            options.Add(new MenuOption("4", "Intoarcere la meniul principal", UserChoice.PreviousMenu));
            options.Add(new MenuOption("5", "Iesire", UserChoice.Exit));
        }

        public static void UserMenu(out List<MenuOption> options, out string action)
        {
            action = "Selectati actiunea dorita";
            options = new List<MenuOption>(4);
            options.Add(new MenuOption("1", "Informatii despre o ruta", UserChoice.Route));
            options.Add(new MenuOption("2", "Afisarea tuturor oraselor", UserChoice.List));
            options.Add(new MenuOption("3", "Intoarcere la meniul principal", UserChoice.PreviousMenu));
            options.Add(new MenuOption("4", "Iesire", UserChoice.Exit));
        }

    }
}
