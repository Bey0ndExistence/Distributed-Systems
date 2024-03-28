/**************************************************************************
 *                                                                        *
 *  File:        Program.cs                                               *
 *  Copyright:   (c) 2024, Moloman Laurentiu-Ionut                        *
 *  E-mail:      laurentiu-ionut.moloman@student.tuiasi.ro                *
 *  Description: TransportInfo application with MVC architecture.         *
 *               Program class.(Software Engineering lab 6)               *
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
using View;


namespace TransportInfo
{
    static class Program
    {
        static void Main()
        {
            IModel model = new Model();
            IView view = new ConsoleView(model);
            IPresenter presenter = new Presenter.Presenter(view, model);
            view.SetPresenter(presenter);
            ((ConsoleView)view).Start();
        }
    }

}
