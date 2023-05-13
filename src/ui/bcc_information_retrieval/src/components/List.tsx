import React from "react";
import {useState,useEffect} from "react";
import {ArticleListItem} from "../lib/global/datatypes";
import ReactPaginate from 'react-paginate';
import "../assets/list.css"
import ArticlePreview from "./ArticlePreview";

const List = (props:{list:ArticleListItem[], pageCount:number, pageFunction:Function}) =>{
    const [pageCount,setPageCount] = useState<number>(1);

    useEffect(() => {
        setPageCount(props.pageCount);
    })
    
    const list = props.list;
    const items = list.map(({id,author,title,content}:ArticleListItem,index)=>{
        return <ArticlePreview article_id={id} author={author} title={title} articleSnippet={content} key={index}/>
    });


    const handlePageClick = (event:any) => {
        const pageNumber:number = (event.selected) + 1;
        props.pageFunction(pageNumber);
      };
    

    
    return(
        <div className={"container"}>
            <ul className={"document-list"}>
                {items}           
            </ul>
        <div className={"paging-container"}>
        <ReactPaginate
          breakLabel="..."
          nextLabel="next >"
          onPageChange={handlePageClick}
          pageRangeDisplayed={5}
          pageCount={pageCount}
          previousLabel="< previous"
          renderOnZeroPageCount={null}
        />
        </div>
        </div>
    )


}

export default List;