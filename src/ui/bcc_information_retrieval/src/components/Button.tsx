import React from "react";
import "../assets/Button.css"
//Komponenta reprezentujici tlacitko v HTML
const Button = (props:{ className:string, text:string, id:string})=>{

    //trida, kterou tlacitko dostane - pro chyceni CSS pravidel
    const className = props.className;
    //text, ktery v sobe tlacitko bude mit
    const text = props.text;
    // identifikator elementu
    const id = props.id;
    //samotne vraceni tlacitka v JSX syntaxi (babel provadi preklad)
    return (
    <button type={"submit"} className={className} id={id}>{text}</button>
    )
}

//Modulove orientovane - vyexportujeme celou funkci, aby komponentu bylo mozne pouzit
export default Button;