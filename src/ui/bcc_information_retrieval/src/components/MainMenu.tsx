import React from "react"
import {Menu} from "../lib/global/datatypes";






//nazev polozky + kam nas ma reroutnout
//definice hlavniho menu
//hardcoded, prakticky nevhodne, v ramci aplikace nerespektuji zadna prava
//realne vhodne mit globalni stav (state), ktery bude drzet informaci, zda je uzivatel prihlasen a jaka ma prava
//dle toho by se zmenily polozky menu
//pripadne by mohlo vzniknout vice komponent, napriklad </PublicMenu>, <AdminMenu/>, <UserLoggedInMenu/>

const MainMenu = (props:Menu) =>{
    const menuTitle: string = props.menuTitle;
    return(
        <h2 className={"subtitle center-title"}> {menuTitle} </h2>
            )
}

export default MainMenu;