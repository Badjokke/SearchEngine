import React from "react";
import {useState} from "react";
import requestDocuments from "../../api/SearchAPI";
import SearchBar from "../../components/SearchBar";
import Button from "../../components/Button";




const Search = () => {

    const fetchSearchResults = async (event:React.FormEvent) =>{
        event.preventDefault();
        const res = await requestDocuments(searchQuery);
        console.log(res);
    }

    const [searchQuery, setSearchQuery] = useState("");
    const [responseMessage, setResponseMessage] = useState("");
    const containerId: string = "search-bar-form";
    return(
        <div id={"search-bar-container"}>
            <h1 className={"subtitle"}>Vyhledejte informaci</h1>
            <form onSubmit={fetchSearchResults}>
                <SearchBar containerId={containerId} query={searchQuery} setQuery={setSearchQuery}/>
                <Button className={"button button-submit"} id={"submit_button"} text={"Vyhledat dokumenty"}/>

            </form>
        </div>
    )
}
export default Search;
