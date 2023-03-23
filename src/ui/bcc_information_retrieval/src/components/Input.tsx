import React from "react";
import {InputType} from "../lib/global/datatypes";
import "../assets/Input.css"


const Input = (props:InputType) => {
    return(
      <input
          type={props.type}
          id={props.id}
          placeholder={props.placeholder}
          onInput={e=>props.onInput(e.currentTarget.value)}
          value={props.value}
          className={props.className}
      />
    );
}

export default Input;