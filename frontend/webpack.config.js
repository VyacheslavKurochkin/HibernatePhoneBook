const path = require("path");

const {CleanWebpackPlugin} = require("clean-webpack-plugin");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const { VueLoaderPlugin } = require("vue-loader");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const webpack = require("webpack");

module.exports = {
    devtool: "source-map",

    target: ["web", "es5"],

    entry: "./js/script.js",

    output: {
        filename: "script.js",
        path: path.resolve(__dirname, "../src/main/resources/static")
    },

    devServer: {
        hot: true,
        open: true,
        proxy: {
            "/api": "http://localhost:3000"
        }
    },

    module:{
        rules:[
            {
                test: /\.css$/,
                use: [
                    MiniCssExtractPlugin.loader, "css-loader"
                ]
            },
            {
                test: /\.vue$/,
                use: "vue-loader"
            },
            {
                test: /\.(png|jpg|gif|svg|ttf|eot|woff|woff2)$/,
                type: "asset/resource"
            }
        ]
    },

    plugins:[
        new CleanWebpackPlugin(),
        new MiniCssExtractPlugin({
            filename: "styles.css"
        }),
        new VueLoaderPlugin(),
        new webpack.DefinePlugin({
            __VUE_PROD_DEVTOOLS__: JSON.stringify(false)
        }),
        new HtmlWebpackPlugin({
            template: "index.html"
        })
    ]
};