import React from "react";
import uploadArticle from "../../api/UploadAPI";
import "../../assets/Upload.css"
import {useState} from "react"
const Upload = () =>{
    const [file,setFile] = useState<File>();
    
    const handleFileChange = (e: any) => {
        if (e.target.files) {
          setFile(e.target.files[0]);
        }
      };
      const handleUploadClick = async () => {
        if (!file) {
          return;
        }
        const response = await uploadArticle(file);
        alert(response.message);
      };

    return(
        <div className="upload-container">
            <input type="file" onChange={handleFileChange} />

            <div>{file && `${file.name} - ${file.type}`}</div>

            <button onClick={handleUploadClick}>Upload</button>
        </div>
    )
}
export default Upload;
