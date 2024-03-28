/**************************************************************************
 *                                                                        *
 *  File:        Calculator.cs                                            *
 *  Copyright:   (c) 2024, Moloman Laurentiu-Ionut                        *
 *  E-mail:      laurentiu-ionut.moloman@student.tuiasi.ro                *
 *  Description: TransportInfo application with MVC architecture.         *
 *               Calculator class.(Software Engineering lab 6)            *
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

namespace Presenter
{
    class Calculator
    {
        /// <summary>
        /// Distance in km between two locations on Earth using Haversine's formula
        /// </summary>
        public static double Distance(City c1, City c2)
        {
            // calculeaza distanta in kilometri intre doua puncte de pe suprafata Pamantului
            // identificate prin latitudine si longitudine, folosind formula lui Haversine

            double lat1 = c1.Latitude * Math.PI / 180.0;
            double long1 = c1.Longitude * Math.PI / 180.0;
            double lat2 = c2.Latitude * Math.PI / 180.0;
            double long2 = c2.Longitude * Math.PI / 180.0;

            double dLat = lat2 - lat1;
            double dLong = long2 - long1;

            double a = Math.Sin(dLat / 2) * Math.Sin(dLat / 2) + Math.Cos(lat1) * Math.Cos(lat2) * Math.Sin(dLong / 2) * Math.Sin(dLong / 2);
            double c = 2 * Math.Atan2(Math.Sqrt(a), Math.Sqrt(1 - a));

            double radius = 6371; // raza Pamantului in km
            return radius * c; // distanta in km
        }

        /// <summary>
        /// Returns the cost for a given distance
        /// </summary>
        public static double Cost(double distance)
        {
            // o functie de calcul al costului    
            return (5.0 + distance / 30.0) * 5;
        }
    }
}
