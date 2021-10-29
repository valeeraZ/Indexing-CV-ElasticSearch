import Layout from '../components/layout'
import getCVById from "../lib/getCVById";
import ListItemText from "@mui/material/ListItemText";
import * as React from "react";
import Typography from "@mui/material/Typography";


export default function CV({ data }){
    const username = data.username
    const attachment = data.attachment
    const base64 = data.data
    const str = "data:application/pdf;base64," + base64
    return(
        <Layout>
            <Typography variant="h6" gutterBottom component="div" mt={3}>
                {username}&apos;s CV
            </Typography>
            <ListItemText
                primary={username}
                secondary={
                    <React.Fragment>
                        <Typography
                            sx={{ display: 'inline' }}
                            component="span"
                            variant="body1"
                            color="text.primary"
                        >
                            Contents:
                        </Typography>
                        {attachment.content}
                    </React.Fragment>
                }
            />
            <iframe
                src={str}
                width="100%"
                height="1000px">
            </iframe>
        </Layout>
    )
}

export async function getServerSideProps({query}){
    console.log(query)
    const data = await getCVById(query.id)
    return {
        props: {
            data
        }
    }
}