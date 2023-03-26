import React from "react"
import {Menu} from "../lib/global/datatypes";





const MainMenu = (props:Menu) =>{
    const menuTitle: string = props.menuTitle;
    return(
        <h2 className={"subtitle center-title"}> {menuTitle} </h2>
            )
}

export default MainMenu;