import React from "react";
import { RadioButtonGroupItem } from "../lib/global/datatypes";
import "../assets/RadioButtonGroup.css"

const RadioButtonGroup = (props:{items:RadioButtonGroupItem[],setModelValue:Function}) =>{
    const radioButtons = props.items.map((value,index) => {
        return <label key={index}><input key={index} onInput={e=>props.setModelValue(e.currentTarget.value)} type="radio" value={`${value.value}`} name={`${value.name}`} />{value.text}</label>
    });
    return(
        <div className="radio-button-container">
            {radioButtons}
        </div>
    )
}

export default RadioButtonGroup;