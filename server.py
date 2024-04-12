"""
This program runs the API.
When running, it is accepting requests from the app.
It's purpose is to convert a PDF of sheet music into an MP3 file.

Author: David Kelly
Date: 12th April 2024
"""

from fastapi import FastAPI, UploadFile, BackgroundTasks
from fastapi.responses import FileResponse
import uvicorn
import aiofiles
import subprocess

app = FastAPI()

@app.get("")


@app.post("/uploadfile/")
async def create_upload_file(file: UploadFile): # Writes the PDF to this desktop's storage.
    async with aiofiles.open(f"./temporary/{file.filename}", mode="wb") as out_file:
        content = await file.read()
        await out_file.write(content)

    # Audiveris is called to read the given PDF, and export the corresponding MusicXML.
    # Raw Terminal Command = audiveris -batch -export '\path\sheet-music.pdf'
    subprocess.run(["audiveris", "-batch", "-export", "-output", "temporary", f"temporary/{file.filename}"], shell=True, check=True)
    
    # MuseScore is called to read in the MusicXML file and convert it to an MP3.
    # Raw Terminal Command = MuseScore4.exe -o "path\to\output.mp3" "path\to\input.mxl"
    subprocess.run(["MuseScore4.exe", "-o", f"temporary/{file.filename.replace('.pdf', '.mp3')}", f"temporary/{file.filename.replace('.pdf', '.mxl')}"], shell=True, check=True)

    # Confirm that the task is complete.
    print("Success! File Conversion Finished!")

    # Send the MP3 file back to the StaveTime app
    return FileResponse(f"temporary/{file.filename.replace('.pdf', '.mp3')}")


def read_root():

    return {"Hello": "World"}

@app.get("/users/{input}")

def read_item(input: str):

    return {"text": input}

# Launches the API as a script.
if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8080)

