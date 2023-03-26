import React from "react"
//naimportime samotny router - je nutne jej nainstalovat
import {BrowserRouter, Routes, Route} from "react-router-dom";
//naimpotime konfiguraci stranek, ktere mame vytvorene
import {pages} from "./pagesData";
//vlastni datovy typ pro itemy, kterym router rozumi
import {RouterType} from "../lib/global/datatypes";

const Router = () =>{
    //vytvorime jednotlive routes
    //title je nadpis, ktery bude element mit, napriklad "Nahrat soubor"
    //path je samotne URL, ktere bude poslouchano a smerovana na konkretni element
    //element je samotna komponenta, ktera bude zavolana pri samotnem requestu
    const routes = pages.map(({ path, title, element }: RouterType) =>{
        return <Route key={title} path= {path} element={element}/>
    });
    return(
        //obalime jednotlive routes samotnym routerem
        <BrowserRouter>
            <Routes>
                {routes}
            </Routes>
        </BrowserRouter>
    )
}
export default Router;
