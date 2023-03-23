import React, {ButtonHTMLAttributes} from "react";





//todo podat type jako prop
const Button = (props:{ className:string, text:string, id:string})=>{

    //const type = props.type;
    const className = props.className;
    const text = props.text;
    const id = props.id;

    return (
    <button type={"submit"} className={className} id={id}>{text}</button>
    )
}
export default Button;