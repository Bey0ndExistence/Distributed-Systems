/**************************************************************************
 *                                                                        *
 *  File:        IModel.cs                                                 *
 *  Copyright:   (c) 2008-2024, Florin Leon                               *
 *  E-mail:      florin.leon@academic.tuiasi.ro                           *
 *  Website:     http://florinleon.byethost24.com/lab_ip.html             *
 *  Description: TransportInfo application with MVC architecture.         *
 *               Model class. (Software Engineering lab 6)                *
 *                                                                        *
 *  This program is free software; you can redistribute it and/or modify  *
 *  it under the terms of the GNU General Public License as published by  *
 *  the Free Software Foundation. This program is distributed in the      *
 *  hope that it will be useful, but WITHOUT ANY WARRANTY; without even   *
 *  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR   *
 *  PURPOSE. See the GNU General Public License for more details.         *
 *                                                                        *
 **************************************************************************/

namespace TransportInfo
{
    public interface IModel
    {
        bool Add(City city);

        bool DataExists();

        bool Delete(string cityName);

        int CityCount { get; }

        void InitializeData();

        string ListAll();

        bool SaveData();

        bool Exists(string cityName);

        City Search(string cityName);
    }
}
