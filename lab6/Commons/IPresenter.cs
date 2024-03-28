/**************************************************************************
 *                                                                        *
 *  File:        IPresenter.cs                                            *
 *  Copyright:   (c) 2024, Moloman Laurentiu-Ionut                        *
 *  E-mail:      laurentiu-ionut.moloman@student.tuiasi.ro                *
 *  Description: TransportInfo application with MVC architecture.         *
 *  IPresenter class. (Software Engineering lab 6)                        *
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

namespace Commons
{
    public interface IPresenter
    {
        void AddCity(City c);

        bool CityExists(string name);

        void ComputeRoute(string city1, string city2);

        void Exit();

        City GetCity(string name);

        void Init();

        void RemoveCity(string name);
    }
}
