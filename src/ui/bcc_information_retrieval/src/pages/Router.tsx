import React from "react"
import {BrowserRouter, Routes, Route} from "react-router-dom";
import {pages} from "./pagesData";
import {RouterType} from "../lib/global/datatypes";

const Router = () =>{
    const routes = pages.map(({ path, title, element }: RouterType) =>{
        return <Route key={title} path= {path} element={element}/>
    });
    return(
        <BrowserRouter>
            <Routes>
                {routes}
            </Routes>
        </BrowserRouter>
    )
}
export default Router;