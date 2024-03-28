/**************************************************************************
 *                                                                        *
 *  File:        Presenter.cs                                             *
 *  Copyright:   (c) 2024, Moloman Laurentiu-Ionut                        *
 *  E-mail:      laurentiu-ionut.moloman@student.tuiasi.ro                *
 *  Description: TransportInfo application with MVC architecture.         *
 *               Presenter class.(Software Engineering lab 6)            *
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
using System.Threading;
using System.Threading.Tasks;
using Commons;

namespace Presenter
{
    public class Presenter : IPresenter
    {
        private IModel _model;
        private IView _view;

        public Presenter(IView view, IModel model)
        {
            _view = view;
            _model = model;
        }

        public void Init()//trebuie apelata pentru a initializa prezentarea
        {
            if (!_model.DataExists())
            {
                _view.Display("Fisierul cu orase nu exista." + Environment.NewLine, "red");
            }
            else
            {
                _model.InitializeData();
                _view.Display("Fisier incarcat: " + _model.CityCount + " orase." + Environment.NewLine, "magenta");
            }
        }

        public void Exit()
        {
            if (_model.SaveData())
                _view.Display("Fisierul a fost salvat." + Environment.NewLine, "magenta");
            _view.Display("La revedere.", "default");
            // Application.DoEvents(); // numai pentru Windows Forms
            Thread.Sleep(1000);
            Environment.Exit(0);
        }
        public City GetCity(string name)
        {
            return _model.Search(name);
            //bruh
        }

        public void RemoveCity(string name)
        {
            if (_model.Delete(name))
            {
                _view.Display("\nOrasul " + name + " a fost sters.\n", "blue");
            }
        }

        public void AddCity(City city)
        {
            bool cityExists = CityExists(city.Name);
            if (cityExists)
            {
                _view.Display($"\nOrasul {city.Name} este deja inregistrat.\n", "red");
            }
            else
            {
                _model.Add(city);
                _view.Display($"\nOrasul {city.Name} a fost adaugat cu succes.\n", "blue");
            }
        }

        public bool CityExists(string name)
        {
            return _model.Exists(name);
        }

        public void ComputeRoute(string city1, string city2)
        {
            if (!CityExists(city1) || !CityExists(city2))
            {
                _view.Display("Unul sau ambele orase nu exista.", "red");
                return;
            }

            City sourceCity = GetCity(city1);
            City destinationCity = GetCity(city2);

            double distance = Calculator.Distance(sourceCity, destinationCity);
            double cost = Calculator.Cost(distance);

            _view.Display($"Distanta: {city1}-{city2}: {distance} km", "green");
            _view.Display($"Costul: {cost} lei\n", "green");
        }
    }
}
