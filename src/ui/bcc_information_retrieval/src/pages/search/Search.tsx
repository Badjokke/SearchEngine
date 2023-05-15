import React from "react";
import {useState} from "react";
import requestDocuments from "../../api/SearchAPI";
import SearchBar from "../../components/SearchBar";
import Button from "../../components/Button";
import List from "../../components/List";
import {ArticleListItem} from "../../lib/global/datatypes";
import RadioButtonGroup from "../../components/RadioButtonGroup";
import { ButtonType } from "../../lib/global/datatypes";
const Search = () => {
    //vlastni funkce, ktera se zavola pri odeslani formulare
    //event.preventDefault() je dulezity, jinak dojde k redirectu tam, kam formular ukazuje
    //v tomto pripade neukazuje "nikam" - provede se refresh stranky, to urcite neni chtene chovani
    const fetchSearchResults = async (event:React.FormEvent) =>{
        event.preventDefault();
        if(!searchQuery){
            alert("No query provided.");
            return;
        }
        const res = await fetchDocuments(searchQuery,selectedModel);
        const documents = res.articles;
        const pageCount = res.pageCount;
        setDocuments(documents);
        setPageCount(pageCount);
    }
    const fetchDocuments = async (searchQuery: string, searchVector:number=1, page:number=1):Promise<{articles:ArticleListItem[],pageCount:number}> => {
        return requestDocuments(searchQuery,searchVector,page);
    }

    const fetchPage =async (pageNumber:number, vectorModel: number) => {
       
        const res = await fetchDocuments(searchQuery,vectorModel,pageNumber);
        const documents = res.articles;
        setDocuments(documents);
    }


    //samotny stav komponent - perzistentni uloziste v ramci zivotniho cyklu komponenty
    //co tato syntaxe semanticky rika - vytvor promennou searchQuery a funkce setSearchQuery, ktera nastavi hodnotu teto
    //promenne
    // useState("") - defaultni hodnota promenne searchQuery je nic (undefined)
    //tato syntaxe neni typescriptova - je mozne ji pouzit, ale vhodnejsi je konstrukce nize
    const [searchQuery, setSearchQuery] = useState("");
    //Ekvivalent konstrukce vyse, pouze je zde zanesen vlastni datovy typ ArticleListItems
    //tim tedy deklarujeme, ze promenna retrievedDocuments je typu ArticleListItems
    const [retrievedDocuments, setDocuments] = useState<ArticleListItem[]>([]);
    const [pageCount,setPageCount] = useState<number>(0);
    const [selectedModel,setSelectedModel] = useState<number>(1);


    //id inputu
    const containerId: string = "search-bar-form";
    //vykresli celou stranku tvorenou komponentami
        return(
        <div className={"container"}>
            <div id={"search-bar-container"}>
                <h1 className={"subtitle"}>Vyhledejte informaci</h1>
                <form onSubmit={fetchSearchResults}>
                    <SearchBar containerId={containerId} query={searchQuery} setQuery={setSearchQuery}/>
                    <Button className={"button button-submit"} type={ButtonType.SUBMIT} id={"submit_button"} text={"Vyhledat dokumenty"}/>
                    <RadioButtonGroup items={[{value:"1",name:"model",text:"tf-idf"},{value:"2",name:"model",text:"bag-of-words"},{value:"3",name:"model",text:"boolean"}]} setModelValue={setSelectedModel}/>
                </form>
                {retrievedDocuments.length !== 0 && <List list={retrievedDocuments} pageCount={pageCount} pageFunction={fetchPage}/>}
            </div>
        </div>
    )
}
export default Search;
