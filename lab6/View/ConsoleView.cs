/**************************************************************************
 *                                                                        *
 *  File:        ConsoleView.cs                                           *
 *  Copyright:   (c) 2024, Moloman Laurentiu-Ionut                        *
 *  E-mail:      laurentiu-ionut.moloman@student.tuiasi.ro                *
 *  Description: TransportInfo application with MVC architecture.         *
 *               ConsoleView class.(Software Engineering lab 6)           *
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
using Commons;

namespace View
{
    public class ConsoleView : IView
    {
        private IPresenter _presenter;
        private IModel _model;
        private List<Menus.MenuOption> _menuOptions;

        public ConsoleView(IModel model)
        {
            _model = model;
        }

        public void Start()
        {
            Menus.UserChoice choice = Menus.UserChoice.Undefined;
            Menus.MenuState menuState = Menus.MenuState.Main;

            while (choice != Menus.UserChoice.Exit)
            {
                // preia comanda in functie de starea curenta a meniului

                choice = GetMenuOption(menuState);

                switch (choice)
                {
                    // comenzi
                    case Menus.UserChoice.Route:
                        string cn1, cn2;
                        GetTwoCities(out cn1, out cn2);
                        _presenter.ComputeRoute(cn1, cn2);
                        break;
                    case Menus.UserChoice.RemoveCity:
                        string cityName = GetCity();
                        _presenter.RemoveCity(cityName);
                        break;
                    case Menus.UserChoice.AddCity:
                        City c = InputCity();
                        _presenter.AddCity(c);
                        break;
                    case Menus.UserChoice.List:
                        ListAll();
                        break;
                    case Menus.UserChoice.Exit:
                        _presenter.Exit();
                        break;

                    // navigare meniuri
                    case Menus.UserChoice.AdminMenu:
                        menuState = Menus.MenuState.Administrator;
                        break;
                    case Menus.UserChoice.UserMenu:
                        menuState = Menus.MenuState.User;
                        break;
                    case Menus.UserChoice.PreviousMenu:
                        menuState = Menus.MenuState.Main;
                        break;
                }
            }
        }

        private Menus.UserChoice GetMenuOption(Menus.MenuState menuType)
        {
            string action = "";

            switch (menuType)
            {
                case Menus.MenuState.Main:
                    Menus.MainMenu(out _menuOptions, out action);
                    break;
                case Menus.MenuState.Administrator:
                    Menus.AdminMenu(out _menuOptions, out action);
                    break;
                case Menus.MenuState.User:
                    Menus.UserMenu(out _menuOptions, out action);
                    break;
            }
            Menus.UserChoice choice = Menus.UserChoice.Undefined;
            while (choice == Menus.UserChoice.Undefined)
            {
                Display(action + Environment.NewLine, "yellow");

                for (int i = 0; i < _menuOptions.Count; i++)
                    Display(_menuOptions[i].Number + ". " + _menuOptions[i].Text, "default");

                Console.Write(Environment.NewLine + "Optiunea dumneavoastra: ");
                string userChoiceNumber = Console.ReadLine();
                Console.WriteLine();

                for (int i = 0; i < _menuOptions.Count; i++)
                {
                    if (userChoiceNumber == _menuOptions[i].Number)
                    {
                        choice = _menuOptions[i].Choice;
                        break;
                    }
                }
            }
            return choice;
        }

        private void GetTwoCities(out string cityName1, out string cityName2)
        {
            string city1 = null, city2 = null;

            Console.Write("Orasul de imbarcare: ");
            city1 = Console.ReadLine();
            while (!_model.Exists(city1))
            {
                Console.WriteLine("Orasul introdus nu exista.");
                Console.Write("Orasul de imbarcare: ");
                city1 = Console.ReadLine();
            }

            Console.Write("Orasul unde coborati: ");
            city2 = Console.ReadLine();
            while (!_model.Exists(city2))
            {
                Console.WriteLine("Orasul introdus nu exista.");
                Console.Write("Orasul unde coborati: ");
                city2 = Console.ReadLine();
            }

            cityName1 = city1;
            cityName2 = city2;
        }

        string GetCity()
        {
            string city = null;
            Console.Write("Introduceti numele orasului: ");
            city = Console.ReadLine();
            while (!_model.Exists(city))
            {
                Console.WriteLine("Orasul introdus nu exista.");
                Console.Write("Introduceti numele orasului: ");
                city = Console.ReadLine();
            }
            return city;
        }

        City InputCity()
        {
            string cityName = null;
            double cityLatitude = 0.0, cityLongitude = 0.0;

            while (string.IsNullOrEmpty(cityName))
            {
                Console.Write("Introduceti numele orasului: ");
                cityName = Console.ReadLine();
                if (string.IsNullOrEmpty(cityName))
                {
                    Console.WriteLine("Numele orasului nu poate fi gol.");
                }
            }

            bool validLatitude = false;
            while (!validLatitude)
            {
                Console.Write("Introduceti latitudinea orasului: ");
                string latitudeInput = Console.ReadLine();
                if (double.TryParse(latitudeInput, out cityLatitude))
                {
                    validLatitude = true;
                }
                else
                {
                    Console.WriteLine("Eroare, format gresit pentru latitudine.");
                }
            }

            bool validLongitude = false;
            while (!validLongitude)
            {
                Console.Write("Introduceti longitudinea orasului: ");
                string longitudeInput = Console.ReadLine();
                if (double.TryParse(longitudeInput, out cityLongitude))
                {
                    validLongitude = true;
                }
                else
                {
                    Console.WriteLine("Eroare, format gresit pentru longitudine.");
                }
            }

            return new City(cityName, cityLatitude, cityLongitude);
        }


        void ListAll()
        {
            Display("Orase: " + _model.ListAll() + "\n", "yellow");
        }

        public void Display(string text, string color)
        {
            ConsoleColor c = ConsoleColor.DarkGray;

            switch (color)
            {
                case "default": c = ConsoleColor.White; break;
                case "red": c = ConsoleColor.Red; break;
                case "green": c = ConsoleColor.Green; break;
                case "blue": c = ConsoleColor.Blue; break;
                case "yellow": c = ConsoleColor.Yellow; break;
                case "magenta": c = ConsoleColor.Magenta; break;
            }

            Console.ForegroundColor = c;
            Console.WriteLine(text);
        }

        public void SetPresenter(IPresenter presenter)
        {
            _presenter = presenter;
            _presenter.Init();
        }
    }
}
