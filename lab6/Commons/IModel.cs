/**************************************************************************
 *                                                                        *
 *  File:        IModel.cs                                                *
 *  Copyright:   (c) 2024, Moloman Laurentiu-Ionut                        *
 *  E-mail:      laurentiu-ionut.moloman@student.tuiasi.ro                *
 *  Description: TransportInfo application with MVC architecture.IModel class. 
 *  (Software Engineering lab 6)                                          *
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
    public interface IModel
    {
        //iModel
        bool Add(City city);

        int CityCount { get; }

        bool DataExists();

        bool Delete(string cityName);

        bool Exists(string cityName);

        void InitializeData();

        string ListAll();

        bool SaveData();

        City Search(string cityName);
    }
}
