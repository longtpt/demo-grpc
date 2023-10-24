SRC_DIR="$PWD/src/main/proto"
DST_DIR="$PWD/target/generated-sources/protobuf"
protoc --plugin=protoc-gen-grpc-java=$PATH_TO_PLUGIN
-I=$SRC_DIR --java_out=$DST_DIR --grpc-java_out=$DST_DIR $SRC_DIR/ping.proto

# To compile a proto file and generate Java interfaces out of the service definitions:
#protoc --plugin=protoc-gen-grpc-java=build/exe/java_plugin/protoc-gen-grpc-java \
#  --grpc-java_out="$OUTPUT_FILE" --proto_path="$DIR_OF_PROTO_FILE" "$PROTO_FILE"
#
#protoc --plugin=protoc-gen-grpc-java=build/exe/java_plugin/protoc-gen-grpc-java \
#  --grpc-java_out=lite:"$OUTPUT_FILE" --proto_path="$DIR_OF_PROTO_FILE" "$PROTO_FILE"




1. Tạo sản phẩm
2. Lấy ra danh sách sản phẩm
3. Tạo sản phẩm
4. Lấy ra danh sách sản phẩm
5. Lấy ra sản phẩm đã tồn tại
6. Lấy ra sản phẩm  không tồn tại
7. Xoá sản phẩm
8. Lấy ra danh sách sản phẩm

