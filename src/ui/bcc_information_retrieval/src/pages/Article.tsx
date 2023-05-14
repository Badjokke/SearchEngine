import React from "react";
import {useState,useEffect} from "react";
import requestArticle from "../api/ArticleAPI";
import { ArticleListItem } from "../lib/global/datatypes";
import FullArticle from "../components/FullArticle";
const Article = () => {
    const [articleId,setArticleId] = useState<number>(0);
    const [article,setArticle] = useState<ArticleListItem>();

    useEffect(()=>{
        const search = window.location.search;
        const params = new URLSearchParams(search);
        const id = params.get('id');
        if(id == null){
            setArticleId(-1);
            return;
        }
        const articleId = parseInt(id);
        setArticleId(articleId);

        const fetchArticleData = async()=>{
            const res = await requestArticle(articleId);
            setArticle(res);
            console.log(res);
        }
        fetchArticleData();
    },[]);

    



        return(
            <div className="article-container">
                {(articleId == 0 || !article) && <h1>loading article...</h1>}
                {articleId == -1 && <h1>article id not provided</h1>}
                {article&&<FullArticle article={article}/>}
            </div>
        )

}
export default Article;