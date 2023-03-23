import React from "react"
import "../assets/SearchBar.css"
import Input from "./Input";

const SearchBar = (props: { containerId: string;query:any,setQuery:any })=>{
    const setQuery = props.setQuery;
    const query = props.query;
    const containerId : string = props.containerId;
    const inputId : string = "search-bar-form";

    return (
        <div id={containerId}>
            <label htmlFor={inputId}/>
            <Input type={"text"} id={inputId} placeholder={"Zadejte řetězec"} onInput={setQuery} value={query} className={"input text-input"}/>
        </div>





    )
}
export default SearchBar;
