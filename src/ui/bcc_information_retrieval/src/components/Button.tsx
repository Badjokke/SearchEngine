import React from "react";
import "../assets/Button.css"
import { ButtonType } from "../lib/global/datatypes";
//Komponenta reprezentujici tlacitko v HTML
const Button = (props:{ className:string, text:string, id:string, type:ButtonType, onClick?:Function})=>{

    //trida, kterou tlacitko dostane - pro chyceni CSS pravidel
    const className = props.className;
    //text, ktery v sobe tlacitko bude mit
    const text = props.text;
    // identifikator elementu
    const id = props.id;
    const type = props.type;
    //samotne vraceni tlacitka v JSX syntaxi (babel provadi preklad)
    return (
    <button type={type} className={className} onClick={props.onClick?props.onClick():null} id={id}>{text}</button>
    )
}

//Modulove orientovane - vyexportujeme celou funkci, aby komponentu bylo mozne pouzit
export default Button;