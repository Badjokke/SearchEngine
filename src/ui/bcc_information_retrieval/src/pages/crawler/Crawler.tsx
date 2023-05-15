import React from "react";
import Button from "../../components/Button";
import SearchBar from "../../components/SearchBar";
import { ButtonType } from "../../lib/global/datatypes";
import {useState} from "react";
import crawlPage from "../../api/CrawlerAPI";
import "../../assets/Crawler.css"
const Crawler = () => {
    const [searchQuery, setSearchQuery] = useState("");

    const crawlArticle = async (event:React.FormEvent) =>{
        event.preventDefault();
        const url = searchQuery;
        if(!url){
            alert("No url provided!")
            return;
        }
        const res = await crawlPage(url);
        alert(res.message);
    }

    const containerId = "url_crawl_container";

    return (
        <div id={containerId} className="container">
           <form onSubmit={crawlArticle}>
                    <SearchBar containerId={containerId} query={searchQuery} setQuery={setSearchQuery}/>
                    <Button className={"button button-submit"} type={ButtonType.SUBMIT} id={"submit_button"} text={"Vyhledat dokumenty"}/>
                </form>
        </div>
    )
}
export default Crawler;