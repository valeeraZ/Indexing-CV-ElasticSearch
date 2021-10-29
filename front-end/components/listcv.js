import * as React from 'react';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import Divider from '@mui/material/Divider';
import ListItemText from '@mui/material/ListItemText';
import ListItemAvatar from '@mui/material/ListItemAvatar';
import Avatar from '@mui/material/Avatar';
import Typography from '@mui/material/Typography';
import Link from 'next/link'
import {ListItemButton} from "@mui/material";

export default function ListCV({ data }) {
    return (
        <List >
            {data.map(({ id, username, attachment }) => (
                <div key={id}>
                <Link href={`/cv?id=${id}`} passHref>
                    <ListItem alignItems="flex-start">
                        <ListItemButton>
                        <ListItemAvatar>
                            <Avatar {...stringAvatar(username)} />
                        </ListItemAvatar>
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
                                    {attachment.content.slice(0, 200)} ...
                                </React.Fragment>
                            }
                        />
                        </ListItemButton>
                    </ListItem>
                </Link>
                <Divider variant="inset" component="li" />
                </div>
            ))}
        </List>
    )
}

function stringToColor(string) {
    let hash = 0;
    let i;

    /* eslint-disable no-bitwise */
    for (i = 0; i < string.length; i += 1) {
        hash = string.charCodeAt(i) + ((hash << 5) - hash);
    }

    let color = '#';

    for (i = 0; i < 3; i += 1) {
        const value = (hash >> (i * 8)) & 0xff;
        color += `00${value.toString(16)}`.substr(-2);
    }
    /* eslint-enable no-bitwise */

    return color;
}

function stringAvatar(name) {
    return {
        sx: {
            bgcolor: stringToColor(name),
        },
        children: `${name.split(' ')[0][0]}${name.split(' ')[1][0]}`,
    };
}