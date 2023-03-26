import React from "react";
import {useState, useEffect} from "react";
import requestDocuments from "../../api/SearchAPI";
import SearchBar from "../../components/SearchBar";
import Button from "../../components/Button";
import List from "../../components/List";
import {ArticleListItems} from "../../lib/global/datatypes";

const Search = () => {
    //vlastni funkce, ktera se zavola pri odeslani formulare
    //event.preventDefault() je dulezity, jinak dojde k redirectu tam, kam formular ukazuje
    //v tomto pripade neukazuje "nikam" - provede se refresh stranky, to urcite neni chtene chovani
    const fetchSearchResults = async (event:React.FormEvent) =>{
        event.preventDefault();
        const res = await requestDocuments(searchQuery);
        setDocuments(res);
        setLoaded(true);
    }
    //react hook
    useEffect(()=>{
        //V tomto pripade neni nutne tento hook pouzit, protoze odesilany formular vyvolava fetch na server
        //ale je zde mozne napsat nejakou logiku - napriklad vykreslit progress bar
        //mirne snizime vykon aplikace, ale radikalne zvysime UX
        console.log(retrievedDocuments);
    });
    //samotny stav komponent - perzistentni uloziste v ramci zivotniho cyklu komponenty
    //co tato syntaxe semanticky rika - vytvor promennou searchQuery a funkce setSearchQuery, ktera nastavi hodnotu teto
    //promenne
    // useState("") - defaultni hodnota promenne searchQuery je nic (undefined)
    //tato syntaxe neni typescriptova - je mozne ji pouzit, ale vhodnejsi je konstrukce nize
    const [searchQuery, setSearchQuery] = useState("");
    //Ekvivalent konstrukce vyse, pouze je zde zanesen vlastni datovy typ ArticleListItems
    //tim tedy deklarujeme, ze promenna retrievedDocuments je typu ArticleListItems
    const [retrievedDocuments, setDocuments] = useState<ArticleListItems|undefined>();
    const [loaded,setLoaded] = useState(false);

    //id inputu
    const containerId: string = "search-bar-form";
    //vykresli celou stranku tvorenou komponentami
        return(
        <div className={"container"}>
            <div id={"search-bar-container"}>
                <h1 className={"subtitle"}>Vyhledejte informaci</h1>
                //kdyz je odeslan formular, tak zavolam vlastni funkci - fetchSearchResults
                <form onSubmit={fetchSearchResults}>
                    //pouziti komponent pro vytvoreni obsahuje stranky
                    // a predavani vlastnosti komponenty (containerId, query, setQuery)
                    //dobre pravidlo v reactu - callbacky predavame shora dolu (tedy od rodice detem)
                    // a eventy bublaji zdola nahoru (tedy v potomky nastane udalost a potomek zavola predany callback)
                    <SearchBar containerId={containerId} query={searchQuery} setQuery={setSearchQuery}/>
                    <Button className={"button button-submit"} id={"submit_button"} text={"Vyhledat dokumenty"}/>
                </form>
                //podminene vykresleni komponenty - pokud mam nactena data, tak vykresli komponentu List
                //jinak ne
                {loaded && <List articles={retrievedDocuments?.articles}/>}
            </div>
        </div>
    )
}
export default Search;
