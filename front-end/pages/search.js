import Layout from '../components/layout'
import ListCV from '../components/listcv'
import searchCV from '../lib/searchCV'
import Typography from '@mui/material/Typography';

export default function Search({data}) {
    return (
      <Layout>
        <Typography variant="h6" gutterBottom component="div" mt={3}>
          {data.length} CV found containing the keyword
        </Typography>
        <ListCV data={data}></ListCV>
      </Layout>
    )
  }
  
  export async function getServerSideProps({ query }){
      const keyword = query.keyword
      const data = await searchCV(keyword)
      return {
        props: {
          data
        }
      }
  }