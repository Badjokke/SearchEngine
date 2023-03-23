import React from "react"
import {pagesRefs} from "../pages/pagesData"
import {RouterRefs} from "../lib/global/datatypes"
import "../assets/MenuNavbar.css"

//wrapper pro klienta, aby mohl routovat bez primeho psani do url
//navbar zabali router
const MenuNavbar = () => {
    let key: number = 0;
    const items = pagesRefs.map(({path,name}: RouterRefs)=>{
    return <li key={key++}><a href={path}> {name}</a> </li>
            });


    return(
        <div className={"navigation-menu"}>
            <ul id={"nav-menu"}>
                {items}
            </ul>
        </div>
    )
};

export default MenuNavbar;