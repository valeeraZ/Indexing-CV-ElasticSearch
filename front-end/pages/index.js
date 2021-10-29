import Head from 'next/head'
import Image from 'next/image'
import Layout from '../components/layout'
import ListCV from '../components/listcv'
import getAllCVs from '../lib/getAllCVs'
import styles from '../styles/Home.module.css'

export default function Home({data}) {
  return (
    <Layout>
      <ListCV data={data}/>
    </Layout>
  )
}

export async function getServerSideProps(){
    const data = await getAllCVs()
    return {
      props: {
        data
      }
    }
}
