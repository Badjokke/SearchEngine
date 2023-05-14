import React from "react";
import { ArticleListItem } from "../lib/global/datatypes";
import {useState, useEffect} from "react";
import "../assets/ArticlePreview.css"

const FullArticle = (props:{article:ArticleListItem}) =>{
    const [article,setArticle] = useState<ArticleListItem>();
    useEffect(()=>{
        setArticle(props.article);
    },[])
    
    return (
        <div className="article-preview-container">

      
        <h2 className="article-title">{article?.title}</h2>
        <h3 className="article-author">{article?.author || "No author"}</h3>
        <p className="article-preview">
            {article?.content}
        </p>    
        
          
        </div>

    )
}


export default FullArticle;